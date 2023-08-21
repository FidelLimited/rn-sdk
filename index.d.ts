declare module "fidel-react-native" {
  type ValueOf<T> = T[keyof T];

  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  type MetaData = Record<PropertyKey, any>;

  // https://github.com/FidelLimited/rn-sdk/blob/em/ios/Constants/ProgramTypeConstants.swift
  type ProgramType = {
    transactionStream: "transactionStream";
    transactionSelect: "transactionSelect";
  };

  // https://github.com/FidelLimited/rn-sdk/blob/em/ios/Constants/CountryConstants.swift
  type Country = {
    unitedKingdom: "unitedKingdom";
    canada: "canada";
    ireland: "ireland";
    japan: "japan";
    sweden: "sweden";
    unitedArabEmirates: "unitedArabEmirates";
    unitedStates: "unitedStates";
  };

  // https://github.com/FidelLimited/rn-sdk/blob/em/ios/Constants/ErrorTypeConstants.swift
  type ErrorType = {
    userCanceled: "userCanceled";
    deviceNotSecure: "deviceNotSecure";
    enrollmentError: "enrollmentError";
    verificationError: "verificationError";
    sdkConfigurationError: "sdkConfigurationError";
    unknown: "unknown";
  };

  // https://github.com/FidelLimited/rn-sdk/blob/em/ios/Constants/EnrollmentErrorTypeConstants.swift
  // https://github.com/FidelLimited/rn-sdk/blob/em/android/src/main/java/com/fidelreactlibrary/adapters/ResultsAdapter.java
  type EnrollmentErrorType = {
    cardAlreadyExists: "cardAlreadyExists";
    invalidProgramId: "invalidProgramId";
    invalidSdkKey: "invalidSdkKey";
    inexistentProgram: "inexistentProgram";
    cardConsentIssuerProcessingChargeError: "cardConsentIssuerProcessingChargeError";
    cardConsentDuplicateTransactionError: "cardConsentDuplicateTransactionError";
    cardConsentInsufficientFundsError: "cardConsentInsufficientFundsError";
    cardConsentProcessingChargeError: "cardConsentProcessingChargeError";
    cardConsentIncorrectCardDetailsError: "cardConsentIncorrectCardDetailsError";
    cardConsentCardLimitExceeded: "cardConsentCardLimitExceeded";
    cardConsentErrorGeneric: "cardConsentErrorGeneric";
    unauthorized: "unauthorized";
    unexpected: "unexpected";
  };

  // https://github.com/FidelLimited/rn-sdk/blob/em/ios/Adapters/ErrorResultAdapter.swift
  type EnrollmentErrorSubType = {
    cardAlreadyExists: "cardAlreadyExists";
    inexistentProgram: "inexistentProgram";
    invalidSdkKey: "invalidSdkKey";
    invalidProgramId: "invalidProgramId";
    unexpected: "unexpected";
    unauthorized: "unauthorized";
    issuerProcessingError: "issuerProcessingError";
    duplicateTransactionError: "duplicateTransactionError";
    insufficientFundsError: "insufficientFundsError";
    processingChargeError: "processingChargeError";
    cardDetailsError: "cardDetailsError";
    cardLimitExceededError: "cardLimitExceededError";
  };

  // https://github.com/FidelLimited/rn-sdk/blob/em/ios/Constants/VerificationErrorTypeConstants.swift
  type VerificationErrorType = {
    unauthorized: "unauthorized";
    incorrectAmount: "incorrectAmount";
    maximumAttemptsReached: "maximumAttemptsReached";
    cardAlreadyVerified: "cardAlreadyVerified";
    cardNotFound: "cardNotFound";
    verificationNotFound: "verificationNotFound";
    genericError: "genericError";
    unexpected: "unexpected";
  };

  // https://github.com/FidelLimited/rn-sdk/blob/em/ios/Constants/CardSchemeConstants.swift
  type CardScheme = {
    visa: "visa";
    mastercard: "mastercard";
    americanExpress: "americanExpress";
  };

  // https://github.com/FidelLimited/rn-sdk/blob/em/ios/JSProperties.swift
  export interface FidelConsentText {
    companyName?: string;
    termsAndConditionsUrl?: string;
    privacyPolicyURL?: string;
    programName?: string;
    deleteInstructions?: string;
  }

  // https://github.com/FidelLimited/rn-sdk/blob/em/ios/JSProperties.swift
  export interface FidelOptions {
    bannerImage?: string | number;
    allowedCountries?: ValueOf<Country>[];
    defaultSelectedCountry?: ValueOf<Country>;
    supportedCardSchemes?: ValueOf<CardScheme>[];
    shouldAutoScanCard?: boolean;

    metaData?: MetaData;
  }

  // https://github.com/FidelLimited/rn-sdk/blob/em/ios/JSProperties.swift
  export interface FidelSetupArgs {
    sdkKey: string;
    programId: string;
    programType: ValueOf<ProgramType>;
    options: FidelOptions;
    consentText?: FidelConsentText;
  }

  // https://github.com/FidelLimited/rn-sdk/blob/em/ios/Adapters/ErrorResultAdapter.swift
  export interface FidelError {
    message: ValueOf<EnrollmentErrorType> | ValueOf<VerificationErrorType>;
    subtype?: ValueOf<EnrollmentErrorSubType> | ValueOf<VerificationErrorType>;
    type: ValueOf<ErrorType>;
    date: Date;
  }

  // https://github.com/FidelLimited/rn-sdk/blob/em/ios/Adapters/EnrollmentResultAdapter.swift
  export interface FidelEnrollmentResultValue {
    cardId: string;
    accountId: string;
    programId: string;
    enrollmentDate: Date;
    cardScheme: ValueOf<CardScheme>;
    isLive: boolean;
    cardExpirationYear: number;
    cardExpirationMonth: number;
    cardIssuingCountry: string;
    cardFirstNumbers: string | null;
    cardLastNumbers: string | null;
    metaData?: MetaData;
  }

  // https://github.com/FidelLimited/rn-sdk/blob/em/ios/Adapters/VerificationResultAdapter.swift
  export interface FidelVerificationResultValue {
    cardId: string;
  }

  // node_modules/fidel-react-native/ios/Actors/ResultsObserver.swift
  type EnrollmentResultType = {
    EnrollmentResult: "EnrollmentResult";
    Error: "Error";
    VerificationResult: "VerificationResult";
  };

  export interface FidelErrorResult {
    type: EnrollmentResultType["Error"];
    error: FidelError;
  }
  export interface FidelEnrollmentResult {
    type: EnrollmentResultType["EnrollmentResult"];
    enrollmentResult: FidelEnrollmentResultValue;
  }
  export interface FidelVerificationResult {
    type: EnrollmentResultType["VerificationResult"];
    verificationResult: FidelVerificationResultValue;
  }
  export type FidelSetupCallbackResult =
    | FidelEnrollmentResult
    | FidelVerificationResult
    | FidelErrorResult;

  export default class Fidel {
    static Country: Country;
    static CardScheme: CardScheme;
    static ProgramType: ProgramType;
    static ErrorType: ErrorType;
    static EnrollmentErrorType: EnrollmentErrorType;
    static VerificationErrorType: VerificationErrorType;
    // https://github.com/FidelLimited/rn-sdk/blob/em/ios/Adapters/FidelSetupAdapter.swift
    static setup: (
      options: FidelSetupArgs,
      callback: (result: FidelSetupCallbackResult) => void
    ) => void;
    static start: () => void;
    // https://github.com/FidelLimited/rn-sdk/blob/em/ios/Constants/ResultTypeConstants.swift
    static ENROLLMENT_RESULT: EnrollmentResultType["EnrollmentResult"];
    static ERROR: EnrollmentResultType["Error"];
    static VERIFICATION_RESULT: EnrollmentResultType["VerificationResult"];
  }
}
