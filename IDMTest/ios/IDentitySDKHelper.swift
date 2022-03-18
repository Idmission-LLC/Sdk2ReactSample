//
//  IDentitySDKHelper.swift
//  IDMTest
//
//  Created by Pranjal Lamba on 29/11/21.
//

import Foundation
import IDentitySDK
import IDCapture
import SelfieCapture

class IDentitySDKHelper : NSObject{
  
  @IBAction func initializeSDK(){

    IDentitySDK.templateURL = UserDefaults.templateURL
    IDentitySDK.modelURL = UserDefaults.modelURL
    IDentitySDK.apiBaseURL = UserDefaults.apiBaseURL

    IDCapture.realnessThreshold = UserDefaults.realnessThreshold
    IDCapture.frontDocumentConfidence = UserDefaults.frontDocumentConfidence
    IDCapture.backDocumentConfidence = UserDefaults.backDocumentConfidence
    IDCapture.lowerWidthThresholdTolerance = UserDefaults.lowerWidthThresholdTolerance
    IDCapture.upperWidthThresholdTolerance = UserDefaults.upperWidthThresholdTolerance
    IDCapture.faceMatchMax = UserDefaults.faceMatchMax

    SelfieCapture.minFaceWidth = UserDefaults.minFaceWidth
    SelfieCapture.eyeOpenProbability = UserDefaults.eyeOpenProbability
    SelfieCapture.minHeadEulerAngle = UserDefaults.minHeadEulerAngle
    SelfieCapture.maxHeadEulerAngle = UserDefaults.maxHeadEulerAngle
    SelfieCapture.minRelativeNoseHeight = UserDefaults.minRelativeNoseHeight
    SelfieCapture.maxRelativeNoseHeight = UserDefaults.maxRelativeNoseHeight
    SelfieCapture.labelsConfidenceThreshold = UserDefaults.labelsConfidenceThreshold
    SelfieCapture.faceMaskProbabilityThreshold = UserDefaults.faceMaskProbabilityThreshold
    SelfieCapture.liveFaceProbabilityThreshold = UserDefaults.liveFaceProbabilityThreshold

    let loginId = UserDefaults.loginId
    let password = UserDefaults.password
    let merchantId = UserDefaults.merchantId
    
    IDentitySDK.initializeSDK(loginId: loginId, password: password, merchantId: merchantId) { error in
        if let error = error {
            print("!!! initialize SDK ERROR: \(error.localizedDescription)")
        } else {
            print("!!! initialize SDK SUCCESS")
        }
    }
  }
  
  // 20 - ID Validation
  @IBAction func startIDValidations(instances: UIViewController) {
    ViewController().startIDValidation(instance: instances);
  }
 
  // 10 - ID Validation and Match Face
  @IBAction func startIDValidationAndMatchFaces(instances: UIViewController) {
    ViewController().startIDValidationAndMatchFace(instance: instances);
  }
  
  // 50 - ID Validation And Customer Enroll
  @IBAction func startIDValidationAndCustomerEnrolls(uniqueNumbers: String, instances: UIViewController) {
    ViewController().startIDValidationAndCustomerEnroll(uniqueNumber: uniqueNumbers, instance: instances);
  }
  
  // 175 - Customer Enroll Biometrics
  @IBAction func startCustomerEnrollBiometricss(uniqueNumbers: String, instances: UIViewController) {
    ViewController().startCustomerEnrollBiometrics(uniqueNumber: uniqueNumbers, instance: instances);
  }
  
  // 105 - Customer Verification
  @IBAction func startCustomerVerifications(uniqueNumbers: String, instances: UIViewController) {
    ViewController().startCustomerVerification(uniqueNumber: uniqueNumbers, instance: instances);
  }
  
  // 185 - Identify Customer
  @IBAction func startIdentifyCustomers(instances: UIViewController) {
    ViewController().startIdentifyCustomer(instance: instances);
  }
  
  // 660 - Live Face Check
  @IBAction func startLiveFaceChecks(instances: UIViewController) {
    ViewController().startLiveFaceCheck(instance: instances);
  }
}
