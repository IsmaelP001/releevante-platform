import type { Metadata } from "next";
import { Roboto } from "next/font/google";
import { NextIntlClientProvider } from "next-intl";
import { getMessages } from "next-intl/server";
import { routing } from "@/config/i18n/routing";
import { notFound } from "next/navigation";
import "../globals.css";
import { AppReduxProvider } from "@/redux/provider";
import { QueryProvider } from "@/components/QueryProvider";
export const experimental_ppr = true

const roboto = Roboto({
  weight: ["100", "300", "400", "500", "700"],
  subsets: ["latin"],
  display: "swap",
});

export const metadata: Metadata = {
  title: "Create Next App",
  description: "Generated by create next app",
};

export default async function RootLayout({
  children,
  params: { locale },
}: Readonly<{
  children: React.ReactNode;
  params: { locale: string };
}>) {
  if (!routing.locales.includes(locale as any)) {
    notFound();
  }

  const messages = await getMessages();

  return (
    <html lang={locale}>
      <body className={roboto.className}>
        <QueryProvider>
          <AppReduxProvider>
            <NextIntlClientProvider
              messages={messages}
              locale={locale}
              timeZone="America/Santo_Domingo"
            >
             <div>
              {children}
             </div>
            </NextIntlClientProvider>
          </AppReduxProvider>
        </QueryProvider>
      </body>
    </html>
  );
}
