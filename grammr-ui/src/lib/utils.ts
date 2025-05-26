import { ClassValue, clsx } from 'clsx';
import { twMerge } from 'tailwind-merge';
import {languages} from "@/constant/languages";

export function cn(...inputs: ClassValue[]) {
  return twMerge(clsx(inputs));
}

export const capitalize = (s: string | undefined) => {
  if (typeof s !== 'string') return '';
  return s.charAt(0).toUpperCase() + s.slice(1).toLowerCase();
};

export const fullLanguageName = (code: string) => {
  return languages.filter(lang => lang.code === code)[0]?.name
      || code.toUpperCase();
}