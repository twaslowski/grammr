import {cn} from "@/lib/utils";
import {TypingDots} from "@/chat/components/TypingDots";
import {ApiError} from "@/hooks/useApi";
import {Message} from "@/chat/types/message";

interface Props {
  message: Message;
  isLoading: boolean;
  error: ApiError;
}


export default function MessageComponent({message, isLoading, error}: Props) {
  if (isLoading) {
    return (
        <div
            className={cn(
                "max-w-[70%] px-4 py-2 rounded-xl text-sm whitespace-pre-line",
                "ml-auto bg-blue-100"
            )}
        >
          <TypingDots/>
        </div>
    )
  }

  if (error) {
    return (
        <div
            className={cn(
                "max-w-[70%] px-4 py-2 rounded-xl text-sm whitespace-pre-line",
                "ml-auto bg-red-100 text-red-800"
            )}
        >
          {error.message}
        </div>
    )
  }

  if (!error && !isLoading) {
    return (
        <div
            className={cn(
                "max-w-[70%] px-4 py-2 rounded-xl text-sm whitespace-pre-line",
                message.role === 'assistant'
                    ? "ml-auto bg-blue-100"
                    : "mr-auto bg-gray-100"
            )}
        >
        </div>
    );
  }
}
