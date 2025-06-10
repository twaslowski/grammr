import base64
import boto3
import json

languages = {
  'RU': 'ru-RU',
  'EN': 'en-US',
  'FR': 'fr-FR',
  'DE': 'de-DE',
  'IT': 'it-IT',
  'ES': 'es-ES',
  'PT': 'pt-PT',
}


def lambda_handler(event, context):
  try:
    data = json.loads(event['body'])
    text = data['text']
    language = data.get('language', 'EN').upper()
    voice = data.get('voice', 'Joanna')

    polly_client = boto3.client('polly')
    response = polly_client.synthesize_speech(
        Text=text,
        OutputFormat='mp3',
        LanguageCode=languages[language],
        VoiceId=voice
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
