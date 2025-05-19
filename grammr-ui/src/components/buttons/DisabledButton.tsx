import { useRef, useState } from 'react';
import * as React from 'react';
import {Button} from "@/components/ui/button";

interface DisabledButtonProps {
  children: React.ReactNode;
  tooltipText: string;
  icon?: React.ReactNode;
  className?: string;
}

const DisabledButton = (props: DisabledButtonProps) => {
  const [isTooltipVisible, setIsTooltipVisible] = useState(false);
  const buttonRef = useRef(null);

  return (
    <div className='relative inline-block'>
      <Button
        className={`bg-gray-100 text-gray-400 cursor-not-allowed ${props.className}`}
        onMouseEnter={() => setIsTooltipVisible(true)}
        onMouseLeave={() => setIsTooltipVisible(false)}
        ref={buttonRef}
      >
        {props.icon && <span className='mr-1'>{props.icon}</span>}
        {props.children}
      </Button>

      {isTooltipVisible && (
        <div
          id='tooltip'
          className='absolute bottom-full left-1/2 transform -translate-x-1/2 mb-2 px-3 py-2 bg-gray-800 text-white text-xs rounded shadow-lg whitespace-nowrap'
          style={{ pointerEvents: 'none' }}
        >
          {props.tooltipText}
          <div className='absolute top-full left-1/2 transform -translate-x-1/2 w-2 h-2 bg-gray-800 rotate-45'></div>
        </div>
      )}
    </div>
  );
};

export default DisabledButton;
