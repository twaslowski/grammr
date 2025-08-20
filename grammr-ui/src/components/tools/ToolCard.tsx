import React from 'react';
import Link from 'next/link';
import { LucideIcon } from 'lucide-react';

interface ToolCardProps {
  href: string;
  icon: LucideIcon;
  title: string;
  description: string;
}

const ToolCard: React.FC<ToolCardProps> = ({ href, icon: Icon, title, description }) => {
  return (
    <Link href={href}>
      <div className='group block p-6 bg-white border border-gray-200 rounded-lg shadow-sm hover:shadow-md transition-shadow duration-200 hover:border-gray-300 h-32 w-full'>
        <div className='flex items-center space-x-4 h-full'>
          <div className='flex-shrink-0'>
            <div className='w-12 h-12 bg-blue-100 rounded-lg flex items-center justify-center group-hover:bg-blue-200 transition-colors duration-200'>
              <Icon className='w-6 h-6 text-blue-600' />
            </div>
          </div>
          <div className='flex-1 min-w-0 flex flex-col justify-center'>
            <h3 className='text-lg font-medium text-gray-900 group-hover:text-blue-600 transition-colors duration-200 line-clamp-1'>
              {title}
            </h3>
            <p className='text-sm text-gray-500 mt-1 line-clamp-2'>{description}</p>
          </div>
        </div>
      </div>
    </Link>
  );
};

export default ToolCard;
