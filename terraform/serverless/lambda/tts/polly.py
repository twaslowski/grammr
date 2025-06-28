import base64
import boto3
import json

languages = {
  'RU': {
    'languageCode': 'ru-RU',
    'voiceId': 'Tatyana',
    'engine': 'standard'
  },
  'EN': {
    'languageCode': 'en-US',
    'voiceId': 'Danielle',
    'engine': 'generative',
  },
  'FR': {
    'languageCode': 'fr-FR',
    'voiceId': 'Lea',
    'engine': 'generative'
  },
  'DE': {
    'languageCode': 'de-DE',
    'voiceId': 'Vicki',
    'engine': 'generative'
  },
  'IT': {
    'languageCode': 'it-IT',
    'voiceId': 'Carla',
    'engine': 'generative'
  },
  'ES': {
    'languageCode': 'es-ES',
    'voiceId': 'Lucia',
    'engine': 'generative'
  },
  'PT': {
    'languageCode': 'pt-PT',
    'voiceId': 'Ines',
    'engine': 'neural'
  },
}

def lambda_handler(event, context):
  try:
    data = json.loads(event['body'])
    text = data['text']
    language = data.get('language', 'EN')
    language_code = languages.get(language, languages['EN'])['languageCode']
    voice = languages.get(language, languages['EN'])['voiceId']
    engine = languages.get(language, languages['EN'])['engine']

    polly_client = boto3.client('polly')
    response = polly_client.synthesize_speech(
        Text=text,
        OutputFormat='mp3',
        LanguageCode=language_code,
        VoiceId=voice,
        Engine=engine,
    )

    audio_stream = base64.b64encode(response['AudioStream'].read()).decode(
      'utf-8')

    return {
      'statusCode': 200,
      'headers': {
        'Content-Type': 'audio/mpeg',
        'Content-Disposition': 'inline; filename="speech.mp3"'
      },
      'body': audio_stream,
      'isBase64Encoded': True
    }
  except Exception as e:
    return {
      'statusCode': 500,
      'body': json.dumps({'error': str(e)})
    }
