import React from 'react';
import Link from 'next/link';
import { cn } from '@/lib/utils';

interface StyledLinkProps extends React.ComponentProps<typeof Link> {
  children: React.ReactNode;
  className?: string;
}

const StyledLink: React.FC<StyledLinkProps> = ({ children, className, href, ...props }) => {
  const baseClasses = 'text-blue-600 underline hover:text-blue-800 transition-colors';

  // Auto-detect external links
  const isExternal =
    typeof href === 'string' &&
    (href.startsWith('http://') || href.startsWith('https://') || href.startsWith('mailto:'));

  if (isExternal) {
    return (
      <a
        href={href}
        target='_blank'
        rel='noopener noreferrer'
        className={cn(baseClasses, className)}
      >
        {children}
      </a>
    );
  }

  return (
    <Link href={href} {...props} className={cn(baseClasses, className)}>
      {children}
    </Link>
  );
};

export default StyledLink;
