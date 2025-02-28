"use client";

import { ReactNode } from "react";
import { store } from "./store";
import { Provider } from "react-redux";

type Props = {
  children: ReactNode;
};

export const AppReduxProvider: React.FC<Props> = ({ children }) => (
  <Provider store={store}>{children}</Provider>
);