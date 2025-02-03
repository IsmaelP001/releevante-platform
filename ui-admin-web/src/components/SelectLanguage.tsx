"use client";
import { usePathname, useRouter } from "@/config/i18n/routing";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "./ui/select";
import { useLocale } from "next-intl";
import { useState } from "react";
import { cn } from "@/lib/utils";
import { useSearchParams } from "next/navigation";

export default function SelectLanguage() {
  const NAVBAR_ITEMS = [
    { label: "English", value: "en", iconSrc: "/icons/us-flag.svg" },
    { label: "Spanish", value: "es", iconSrc: "/icons/spain-flag.svg" },
    { label: "French", value: "fr", iconSrc: "/icons/france-flag.svg" },
  ];
  const [open, setOpen] = useState(false);
  const router = useRouter();
  const path = usePathname();
  const searchParams = useSearchParams();
  const locale = useLocale();


  return (
    <Select
    defaultValue={locale}
    open={open}
    onOpenChange={setOpen}
    onValueChange={(localeVal) => {
      const currentPath = `${path}?${searchParams!.toString()!}`;
      router.replace(currentPath, { locale: localeVal as any });
    }}
  >
    <SelectTrigger
      className={cn(
        "w-[70px] custom:w-[125px] py-5 pl-1 pr-2 rounded-3xl",
        open && "bg-primary border-4 border-accent-foreground text-white"
      )}
    >
      <SelectValue placeholder="Language" />
    </SelectTrigger>
    <SelectContent>
      {NAVBAR_ITEMS.map((item) => (
        <SelectItem key={item.label} value={item.value}>
          <div className="flex items-center font-medium gap-2.5 justify-between">
            <img
              className="w-[30px] h-[30px] object-contain"
              src={item.iconSrc}
              alt={`${item.label} flag`}
            />
            <p className="hidden custom:block">{item.label}</p>
          </div>
        </SelectItem>
      ))}
    </SelectContent>
  </Select>
  );
}
