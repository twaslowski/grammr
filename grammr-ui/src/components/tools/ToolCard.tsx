import React from 'react';
import Link from 'next/link';
import { LucideIcon } from 'lucide-react';

interface ToolCardProps {
  href: string;
  icon: LucideIcon;
  title: string;
  description: string;
}

// Consolidated static classes - moved outside component for better performance
const CARD_CLASSES =
  'group block p-6 bg-white border border-gray-200 rounded-lg shadow-sm hover:shadow-md hover:border-gray-300 transition-shadow duration-200 h-32 w-full max-w-sm mx-auto';
const ICON_CLASSES =
  'w-12 h-12 flex-shrink-0 bg-blue-100 group-hover:bg-blue-200 rounded-lg flex items-center justify-center transition-colors duration-200';
const TITLE_CLASSES =
  'text-lg font-medium text-gray-900 group-hover:text-blue-600 transition-colors duration-200 line-clamp-1';
const DESCRIPTION_CLASSES = 'text-sm text-gray-500 mt-1 line-clamp-2';
const CONTENT_LAYOUT = 'flex items-center space-x-4 h-full';
const TEXT_CONTAINER = 'flex-1 min-w-0 flex flex-col justify-center';

const ToolCard: React.FC<ToolCardProps> = ({ href, icon: Icon, title, description }) => {
  return (
    <Link href={href}>
      <div className={CARD_CLASSES}>
        <div className={CONTENT_LAYOUT}>
          <div className={ICON_CLASSES}>
            <Icon className='w-6 h-6 text-blue-600' />
          </div>
          <div className={TEXT_CONTAINER}>
            <h3 className={TITLE_CLASSES}>{title}</h3>
            <p className={DESCRIPTION_CLASSES}>{description}</p>
          </div>
        </div>
      </div>
    </Link>
  );
};

export default ToolCard;
