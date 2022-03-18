//
//  ViewController.swift
//  IDentitySample
//
//  Created by Stefan Kaczmarek on 8/29/21.
//

import UIKit
import IDentitySDK
import IDCapture
import SelfieCapture

extension AdditionalCustomerWFlagCommonData {
    init(serviceOptions options: ServiceOptions) {
        let manualReviewRequired: AdditionalCustomerWFlagCommonData.ManualReviewRequired
        switch options.manualReviewRequired {
        case .yes: manualReviewRequired = .yes
        case .no: manualReviewRequired = .no
        case .forced: manualReviewRequired = .forced
        }

        self = AdditionalCustomerWFlagCommonData(manualReviewRequired: manualReviewRequired,
                                                 bypassAgeValidation: options.bypassAgeValidation ? .yes : .no,
                                                 deDuplicationRequired: options.deDuplicationRequired ? .yes : .no,
                                                 bypassNameMatching: options.bypassNameMatching ? .yes : .no,
                                                 postDataAPIRequired: options.postDataAPIRequired ? .yes : .no,
                                                 sendInputImagesInPost: options.sendInputImagesInPost ? .yes : .no,
                                                 sendProcessedImagesInPost: options.sendProcessedImagesInPost ? .yes : .no,
                                                 needImmediateResponse: options.needImmediateResponse ? .yes : .no,
                                                 deduplicationSynchronous: options.deduplicationSynchronous ? .yes : .no,
                                                 verifyDataWithHost: options.verifyDataWithHost ? .yes : .no,
                                                 idBackImageRequired: options.idBackImageRequired ? .yes : .no,
                                                 stripSpecialCharacters: options.stripSpecialCharacters ? .yes : .no)
    }
}

class ViewController: UIViewController {
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        IDCapture.isDebugMode = UserDefaults.debugMode
        SelfieCapture.isDebugMode = UserDefaults.debugMode
    }

    // MARK: - IBAction Methods

    // 20 - ID Validation
    func startIDValidation(instance: UIViewController) {
        // start ID capture, presenting it from this view controller
      let options = AdditionalCustomerWFlagCommonData(serviceOptions: UserDefaults.serviceOptions)
      IDentitySDK.idValidation(from: instance, options: options) { result, front, back in
            switch result {
            case .success(let request):
                // pass the API request to the success view controller
                let successViewController = SuccessViewController()
                successViewController.customerValidateIdRequest = request
                successViewController.frontDetectedData = front
                successViewController.backDetectedData = back
                let navigationController = UINavigationController(rootViewController: successViewController)
                instance.present(navigationController, animated: true, completion: nil)
            case .failure(let error):
                print(error.localizedDescription)
                self.sendData(text: error.localizedDescription)
            }
        }
      
    }
  
    // 10 - ID Validation and Match Face
    func startIDValidationAndMatchFace(instance: UIViewController) {
        let options = AdditionalCustomerWFlagCommonData(serviceOptions: UserDefaults.serviceOptions)
        IDentitySDK.idValidationAndMatchFace(from: instance, options: options) { result, front, back, faceMatch in
            switch result {
            case .success(let request):
                // pass the API request to the success view controller
                    let successViewController = SuccessViewController()
                    successViewController.customerValidateIdFaceMatchRequest = request
                    successViewController.frontDetectedData = front
                    successViewController.backDetectedData = back
                    successViewController.faceMatch = faceMatch
                    let navigationController = UINavigationController(rootViewController: successViewController)
                    instance.present(navigationController, animated: true, completion: nil)
            case .failure(let error):
                print(error.localizedDescription)
                self.sendData(text: error.localizedDescription)
            }
        }
    }

    // 50 - ID Validation And Customer Enroll
      func startIDValidationAndCustomerEnroll(uniqueNumber: String, instance: UIViewController) {
          let options = AdditionalCustomerWFlagCommonData(serviceOptions: UserDefaults.serviceOptions)
          IDentitySDK.idValidationAndCustomerEnroll(from: instance, options: options) { result, front, back, faceMatch in
              switch result {
              case .success(var request):
                  // pass the API request to the success view controller
                      let successViewController = SuccessViewController()
                      // set the customer's unique number
                      request.customerData.personalData.uniqueNumber = uniqueNumber
                      successViewController.customerEnrollRequest = request
                      successViewController.frontDetectedData = front
                      successViewController.backDetectedData = back
                      successViewController.faceMatch = faceMatch
                      let navigationController = UINavigationController(rootViewController: successViewController)
                      instance.present(navigationController, animated: true, completion: nil)
              case .failure(let error):
                  print(error.localizedDescription)
                  self.sendData(text: error.localizedDescription)
              }
          }
      }
  
    // 175 - Customer Enroll Biometrics
    func startCustomerEnrollBiometrics(uniqueNumber: String, instance: UIViewController) {
        IDentitySDK.customerEnrollBiometrics(from: instance) { result in
            switch result {
            case .success(var request):
                // pass the API request to the success view controller
                    let successViewController = SuccessViewController()
                    // set the customer's unique number
                    request.customerData.personalData.uniqueNumber = uniqueNumber
                    successViewController.customerEnrollBiometricsRequest = request
                    let navigationController = UINavigationController(rootViewController: successViewController)
                    instance.present(navigationController, animated: true, completion: nil)
            case .failure(let error):
                print(error.localizedDescription)
                self.sendData(text: error.localizedDescription)
            }
        }
    }

    // 105 - Customer Verification
    func startCustomerVerification(uniqueNumber: String, instance: UIViewController) {
        IDentitySDK.customerVerification(from: instance) { result in
            switch result {
            case .success(var request):
                // pass the API request to the success view controller
                    let successViewController = SuccessViewController()
                    // set the customer's unique number
                    request.customerData.personalData.uniqueNumber = uniqueNumber
                    successViewController.customerVerifyRequest = request
                    let navigationController = UINavigationController(rootViewController: successViewController)
                    instance.present(navigationController, animated: true, completion: nil)
            case .failure(let error):
                print(error.localizedDescription)
                self.sendData(text: error.localizedDescription)
            }
        }
    }

    // 185 - Identify Customer
    func startIdentifyCustomer(instance: UIViewController) {
        IDentitySDK.identifyCustomer(from: instance) { result in
            switch result {
            case .success(let request):
                // pass the API request to the success view controller
                    let successViewController = SuccessViewController()
                    successViewController.customerIdentifyRequest = request
                    let navigationController = UINavigationController(rootViewController: successViewController)
                    instance.present(navigationController, animated: true, completion: nil)
            case .failure(let error):
                print(error.localizedDescription)
                self.sendData(text: error.localizedDescription)
            }
        }
    }

    // 660 - Live Face Check
    func startLiveFaceCheck(instance: UIViewController) {
        // start selfie capture, presenting it from this view controller
        IDentitySDK.liveFaceCheck(from: instance) { result, _ in
            switch result {
            case .success(let request):
                // pass the API request to the success view controller
                    let successViewController = SuccessViewController()
                    successViewController.customerLiveCheckRequest = request
                    let navigationController = UINavigationController(rootViewController: successViewController)
                    instance.present(navigationController, animated: true, completion: nil)
            case .failure(let error):
                print(error.localizedDescription)
                self.sendData(text: error.localizedDescription)
            }
        }
    }

    private func sendData(text: String) {
      let dict2:NSMutableDictionary? = ["data" : text ]
      let iDMissionSDK = IDMissionSDK()
      iDMissionSDK.getEvent2("DataCallback", dict: dict2 ?? ["data" : "error"])
    }
}
