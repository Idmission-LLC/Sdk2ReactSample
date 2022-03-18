//
//  UserDefaults+IDentitySample.swift
//  IDentitySample
//
//  Created by Stefan Kaczmarek on 10/23/21.
//

import IDentitySDK

struct ServiceOptions: Codable {
    enum ManualReviewRequired: Int, Codable {
        case yes, no, forced
    }

    var manualReviewRequired: ManualReviewRequired
    var bypassAgeValidation: Bool
    var deDuplicationRequired: Bool
    var bypassNameMatching: Bool
    var postDataAPIRequired: Bool
    var sendInputImagesInPost: Bool
    var sendProcessedImagesInPost: Bool
    var needImmediateResponse: Bool
    var deduplicationSynchronous: Bool
    var verifyDataWithHost: Bool
    var idBackImageRequired: Bool
    var stripSpecialCharacters: Bool

    static var `default`: ServiceOptions {
        ServiceOptions(manualReviewRequired: .no,
                       bypassAgeValidation: false,
                       deDuplicationRequired: false,
                       bypassNameMatching: true,
                       postDataAPIRequired: false,
                       sendInputImagesInPost: false,
                       sendProcessedImagesInPost: false,
                       needImmediateResponse: false,
                       deduplicationSynchronous: false,
                       verifyDataWithHost: false,
                       idBackImageRequired: true,
                       stripSpecialCharacters: true)
    }
}

extension UserDefaults {
    static let defaultTemplateURL = "https://kyc.idmission.com/IDS/service/ids/"
    static let defaultModelURL = "https://kyc.idmission.com/IDS/service/integ/idm/thirdparty/trainingModel"
    static let defaultAPIBaseURL = "https://api.idmission.com/v2/"

    static let defaultLoginId = ""
    static let defaultPassword = ""
    static let defaultMerchantId = ""

    static let defaultRealnessThreshold: Float = 0.8
    static let defaultFrontDocumentConfidence: Float = 0.75
    static let defaultBackDocumentConfidence: Float = 0.75
    static let defaultLowerWidthThresholdTolerance: Float = 0.2     // 1.0 - 0.2 = 0.8
    static let defaultUpperWidthThresholdTolerance: Float = 0.05    // 1.0 + 0.05 = 1.05
    static let defaultFaceMatchMax: Float = 0.9

    static let defaultMinFaceWidth: Float = 0.6
    static let defaultEyeOpenProbability: Float = 0.4
    static let defaultMinHeadEulerAngle: Float = -10
    static let defaultMaxHeadEulerAngle: Float = 10
    static let defaultMinRelativeNoseHeight: Float = 0.48
    static let defaultMaxRelativeNoseHeight: Float = 0.67
    static let defaultLabelsConfidenceThreshold: Float = 0.79
    static let defaultFaceMaskProbabilityThreshold: Float = 0.79
    static let defaultLiveFaceProbabilityThreshold: Float = 0.9

    private static let templateURLKey = "templateURL"
    static var templateURL: String {
        get { standard.string(forKey: templateURLKey) ?? defaultTemplateURL }
        set { standard.set(newValue, forKey: templateURLKey) }
    }

    private static let modelURLKey = "modelURL"
    static var modelURL: String {
        get { standard.string(forKey: modelURLKey) ?? defaultModelURL }
        set { standard.set(newValue, forKey: modelURLKey) }
    }

    private static let apiBaseURLKey = "apiBaseURL"
    static var apiBaseURL: String {
        get { standard.string(forKey: apiBaseURLKey) ?? defaultAPIBaseURL }
        set { standard.set(newValue, forKey: apiBaseURLKey) }
    }

    private static let loginIdKey = "loginId"
    static var loginId: String {
        get { standard.string(forKey: loginIdKey) ?? defaultLoginId }
        set { standard.set(newValue, forKey: loginIdKey) }
    }

    private static let passwordKey = "password"
    static var password: String {
        get { standard.string(forKey: passwordKey) ?? defaultPassword }
        set { standard.set(newValue, forKey: passwordKey) }
    }

    private static let merchantIdKey = "merchantId"
    static var merchantId: String {
        get { standard.string(forKey: merchantIdKey) ?? defaultMerchantId }
        set { standard.set(newValue, forKey: merchantIdKey) }
    }

    // MARK: - IDCapture Settings

    private static let realnessThresholdKey = "realnessThreshold"
    static var realnessThreshold: Float {
        get { standard.object(forKey: realnessThresholdKey) as? Float ?? defaultRealnessThreshold }
        set { standard.set(newValue, forKey: realnessThresholdKey) }
    }

    private static let frontDocumentConfidenceKey = "frontDocumentConfidence"
    static var frontDocumentConfidence: Float {
        get { standard.object(forKey: frontDocumentConfidenceKey) as? Float ?? defaultFrontDocumentConfidence }
        set { standard.set(newValue, forKey: frontDocumentConfidenceKey) }
    }

    private static let backDocumentConfidenceKey = "backDocumentConfidence"
    static var backDocumentConfidence: Float {
        get { standard.object(forKey: backDocumentConfidenceKey) as? Float ?? defaultBackDocumentConfidence }
        set { standard.set(newValue, forKey: backDocumentConfidenceKey) }
    }

    private static let lowerWidthThresholdToleranceKey = "lowerWidthThresholdTolerance"
    static var lowerWidthThresholdTolerance: Float {
        get { standard.object(forKey: lowerWidthThresholdToleranceKey) as? Float ?? defaultLowerWidthThresholdTolerance }
        set { standard.set(newValue, forKey: lowerWidthThresholdToleranceKey) }
    }

    private static let upperWidthThresholdToleranceKey = "upperWidthThresholdTolerance"
    static var upperWidthThresholdTolerance: Float {
        get { standard.object(forKey: upperWidthThresholdToleranceKey) as? Float ?? defaultUpperWidthThresholdTolerance }
        set { standard.set(newValue, forKey: upperWidthThresholdToleranceKey) }
    }

    private static let faceMatchMaxKey = "faceMatchMax"
    static var faceMatchMax: Float {
        get { standard.object(forKey: faceMatchMaxKey) as? Float ?? defaultFaceMatchMax }
        set { standard.set(newValue, forKey: faceMatchMaxKey) }
    }

    // MARK: - SelfieCapture Settings

    private static let minFaceWidthKey = "minFaceWidth"
    static var minFaceWidth: Float {
        get { standard.object(forKey: minFaceWidthKey) as? Float ?? defaultMinFaceWidth }
        set { standard.set(newValue, forKey: minFaceWidthKey) }
    }

    private static let eyeOpenProbabilityKey = "eyeOpenProbability"
    static var eyeOpenProbability: Float {
        get { standard.object(forKey: eyeOpenProbabilityKey) as? Float ?? defaultEyeOpenProbability }
        set { standard.set(newValue, forKey: eyeOpenProbabilityKey) }
    }

    private static let minHeadEulerAngleKey = "minHeadEulerAngle"
    static var minHeadEulerAngle: Float {
        get { standard.object(forKey: minHeadEulerAngleKey) as? Float ?? defaultMinHeadEulerAngle }
        set { standard.set(newValue, forKey: minHeadEulerAngleKey) }
    }

    private static let maxHeadEulerAngleKey = "maxHeadEulerAngle"
    static var maxHeadEulerAngle: Float {
        get { standard.object(forKey: maxHeadEulerAngleKey) as? Float ?? defaultMaxHeadEulerAngle }
        set { standard.set(newValue, forKey: maxHeadEulerAngleKey) }
    }

    private static let minRelativeNoseHeightKey = "minRelativeNoseHeight"
    static var minRelativeNoseHeight: Float {
        get { standard.object(forKey: minRelativeNoseHeightKey) as? Float ?? defaultMinRelativeNoseHeight }
        set { standard.set(newValue, forKey: minRelativeNoseHeightKey) }
    }

    private static let maxRelativeNoseHeightKey = "maxRelativeNoseHeight"
    static var maxRelativeNoseHeight: Float {
        get { standard.object(forKey: maxRelativeNoseHeightKey) as? Float ?? defaultMaxRelativeNoseHeight }
        set { standard.set(newValue, forKey: maxRelativeNoseHeightKey) }
    }
    
    private static let labelsConfidenceThresholdKey = "labelsConfidenceThreshold"
    static var labelsConfidenceThreshold: Float {
        get { standard.object(forKey: labelsConfidenceThresholdKey) as? Float ?? defaultLabelsConfidenceThreshold }
        set { standard.set(newValue, forKey: labelsConfidenceThresholdKey) }
    }

    private static let faceMaskProbabilityThresholdKey = "faceMaskProbabilityThreshold"
    static var faceMaskProbabilityThreshold: Float {
        get { standard.object(forKey: faceMaskProbabilityThresholdKey) as? Float ?? defaultFaceMaskProbabilityThreshold }
        set { standard.set(newValue, forKey: faceMaskProbabilityThresholdKey) }
    }

    private static let liveFaceProbabilityThresholdKey = "liveFaceProbabilityThreshold"
    static var liveFaceProbabilityThreshold: Float {
        get { standard.object(forKey: liveFaceProbabilityThresholdKey) as? Float ?? defaultLiveFaceProbabilityThreshold }
        set { standard.set(newValue, forKey: liveFaceProbabilityThresholdKey) }
    }

    // MARK: - Service Options

    private static let serviceOptionsKey = "serviceOptions"
    static var serviceOptions: ServiceOptions {
        get {
            if let data = standard.data(forKey: serviceOptionsKey),
                let options = try? JSONDecoder().decode(ServiceOptions.self, from: data) {
                return options
            } else {
                return .default
            }
        }
        set {
            guard let data = try? JSONEncoder().encode(newValue) else { return }
            standard.set(data, forKey: serviceOptionsKey)
        }
    }

    // MARK: -

    private static let debugModeKey = "debugMode"
    static var debugMode: Bool {
        get { standard.bool(forKey: debugModeKey) }
        set { standard.set(newValue, forKey: debugModeKey) }
    }
}
