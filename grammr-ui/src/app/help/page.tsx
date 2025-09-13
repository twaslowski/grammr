import {
  Accordion,
  AccordionContent,
  AccordionItem,
  AccordionTrigger,
} from '@/components/ui/accordion';
import { Globe, BookOpenCheck, Code2, LightbulbIcon, DownloadIcon } from 'lucide-react';
import React from 'react';
import SyncIcon from '@/components/common/SyncIcon';
import Footer from '@/components/common/Footer';
import StyledLink from '@/components/ui/styled-link';

type FAQItem = {
  question: string;
  answer: React.ReactNode;
};

type FAQSectionProps = {
  id: string;
  title: string;
  icon?: React.ReactNode;
  items: FAQItem[];
};

const generalFaqs: FAQItem[] = [
  {
    question: 'What is Grammr?',
    answer: (
      <>
        <p>
          Grammr is a reference toolkit for grammar and vocabulary. It enables you to deeply
          understand and analyze text in various languages, making it easier to learn and master
          vocabulary and grammar rules.
        </p>
        <p>
          It integrates with <StyledLink href='https://apps.ankiweb.net/'>Anki</StyledLink>, a
          flashcard app, to help you create personalized study decks so you can memorize vocabulary
          and grammar rules effectively.
        </p>
      </>
    ),
  },
  {
    question: 'How do I use Grammr?',
    answer: (
      <div className='space-y-4'>
        <div className='flex items-center gap-x-2'>
          <LightbulbIcon size={36} />
          <p>
            You can chat with an AI-powered tutor that will adapt to your level and help you learn
            new concepts as well as vocabulary for different situations.
          </p>
        </div>
        <div className='flex items-center gap-x-2'>
          <Globe size={36} />
          <p>
            You also have a translator at your disposal which works similarly to conventional
            translation tools, but it also provides you with grammar and vocabulary insights.
          </p>
        </div>
        <p>It is recommended that you also create an account to save your flashcards for export.</p>
      </div>
    ),
  },
  {
    question: 'How do I connect Grammr with Anki?',
    answer: (
      <div className='space-y-4'>
        <p>There are two ways of importing your saved flashcards into Anki:</p>
        <div className='flex items-center gap-x-2'>
          <SyncIcon className='size-6' />
          <p className='font-medium'>Sync via AnkiConnect (Desktop only)</p>
        </div>
        <p className='font-light'>
          Prerequisites: A device running macOS 12+, Windows 10+ or Linux 2023+
        </p>
        <ul className='list-disc list-inside'>
          <li>
            <StyledLink href='https://apps.ankiweb.net/'>
              Install Anki from the official website
            </StyledLink>
          </li>
          <li>
            Install the AnkiConnect add-on from{' '}
            <StyledLink href='https://git.sr.ht/~foosoft/anki-connect#installation'>
              AnkiWeb
            </StyledLink>
            .
          </li>
        </ul>
        <br />

        <div className='flex items-center gap-x-2'>
          <DownloadIcon size={24} />
          <p className='font-medium'>Download and import Deck</p>
        </div>
        <p className='font-light'>Prerequisites: Anki Desktop, AnkiDroid or AnkiMobile installed</p>
      </div>
    ),
  },
];

const languageFaqs: FAQItem[] = [
  {
    question: 'Which languages are supported?',
    answer: (
      <p>
        This site currently only supports Russian, mainly for the sake of simplicity. I am hosting
        this mostly for my own personal use, although a wide variety of languages is supprted
        generally. Inflections are additionally supported for Spanish, French, Portuguese, Italian
        and Romanian. Analysis can be performed for any language that is supported by the{' '}
        <StyledLink href='https://spacy.io/models'>spaCy project</StyledLink>.
      </p>
    ),
  },
  {
    question: 'Can I request a new language?',
    answer: (
      <p>
        Yes! Open a request on{' '}
        <StyledLink href='https://github.com/twaslowski/grammr/issues'>GitHub Issues</StyledLink> or
        reach out directly.
      </p>
    ),
  },
];

const openSourceFaqs: FAQItem[] = [
  {
    question: 'Is Grammr open source?',
    answer: (
      <p>
        Yes, it's licensed under MIT. You can find the source on{' '}
        <StyledLink href='https://github.com/your-org/grammr'>GitHub</StyledLink>.
      </p>
    ),
  },
  {
    question: 'What third-party tools are used?',
    answer: (
      <div>
        <p>We use several open-source libraries, including:</p>
        <ul className='list-disc list-inside'>
          <li>React</li>
          <li>Tailwind CSS</li>
          <li>Radix UI + shadcn/ui</li>
          <li>AnkiConnect</li>
        </ul>
      </div>
    ),
  },
];

export default function Page() {
  return (
    <>
      <div className='container mx-auto px-6 max-w-3xl scroll-mt-24'>
        <section className='text-center py-10'>
          <h1 className='text-4xl font-bold mb-4'>Help & FAQ</h1>
          <p className='text-muted-foreground'>Answers to common questions and usage tips.</p>
        </section>

        <FAQSection
          id='general'
          title='General Questions'
          icon={<BookOpenCheck className='text-primary mr-2' />}
          items={generalFaqs}
        />
        <FAQSection
          id='language-support'
          title='Language Support'
          icon={<Globe className='text-primary mr-2' />}
          items={languageFaqs}
        />
        <FAQSection
          id='open-source'
          title='Open Source & Licensing'
          icon={<Code2 className='text-primary mr-2' />}
          items={openSourceFaqs}
        />
      </div>
      <Footer />
    </>
  );
}

function FAQSection({ id, title, icon, items }: FAQSectionProps) {
  return (
    <section id={id} className='mb-12 scroll-mt-24'>
      <h2 className='text-2xl font-semibold mb-4 flex items-center'>
        {icon}
        {title}
      </h2>
      <Accordion type='multiple' className='space-y-2'>
        {items.map((item, idx) => (
          <AccordionItem key={idx} value={`${id}-item-${idx}`}>
            <AccordionTrigger>{item.question}</AccordionTrigger>
            <AccordionContent>{item.answer}</AccordionContent>
          </AccordionItem>
        ))}
      </Accordion>
    </section>
  );
}
