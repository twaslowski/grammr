import React from 'react';
import { AlertCircle, LucideIcon } from 'lucide-react';

interface ErrorBoxProps {
  title: string;
  children: React.ReactNode;
  icon?: LucideIcon;
  className?: string;
}

export function Error({
  title,
  children,
  icon: Icon = AlertCircle,
  className = '',
}: ErrorBoxProps) {
  return (
    <div
      className={`mb-6 p-5 bg-red-100 text-red-600 rounded-lg border border-red-200 ${className}`}
    >
      <div className='flex items-center mb-3 gap-x-2'>
        <Icon />
        <span className='font-medium'>{title}</span>
      </div>
      <div className='mt-2 text-red-600 text-sm'>{children}</div>
    </div>
  );
}

export default Error;
