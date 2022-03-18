//
//  SuccessViewController.swift
//  IDentitySample
//
//  Created by Stefan Kaczmarek on 10/17/21.
//

import UIKit
import IDentitySDK

class SuccessViewController: UIViewController {
    var customerValidateIdRequest: CustomerValidateIdRequest?                   // 20
    var customerValidateIdFaceMatchRequest: CustomerValidateIdFaceMatchRequest? // 10
    var customerEnrollRequest: CustomerEnrollRequest?                           // 50
    var customerEnrollBiometricsRequest: CustomerEnrollBiometricsRequest?       // 175
    var customerVerifyRequest: CustomerVerifyRequest?                           // 105
    var customerIdentifyRequest: CustomerIdentifyRequest?                       // 185
    var customerLiveCheckRequest: CustomerLiveCheckRequest?                     // 660

    var frontDetectedData: DetectedData?
    var backDetectedData: DetectedData?

    var faceMatch: FaceMatchResult?
    var texts: String!
    var textObfuscated: String!
  
    override func viewDidLoad() {
        super.viewDidLoad()
      
        // pretty print the request object
        let encoder = JSONEncoder()
        encoder.outputFormatting = .prettyPrinted

        // first make sure that the ID Front has been detected, if expected
        if customerValidateIdRequest != nil ||
            customerValidateIdFaceMatchRequest != nil ||
            customerEnrollRequest != nil {
            guard frontDetectedData != nil else {
                texts = "ERROR"
                return
            }
        }
      
        if let customerValidateIdRequest = customerValidateIdRequest {
            // make a copy of the request and stub out the base64 image text for logging
            let request = customerValidateIdRequest
          
            var requestObfuscated = request
            requestObfuscated.customerData.idData.idImageFront = "..."
            if requestObfuscated.customerData.idData.idImageBack != nil {
              requestObfuscated.customerData.idData.idImageBack = "..."
            }
            
            if let data = try? encoder.encode(request),
               let json = String(data: data, encoding: .utf8)  {
                texts = json + "\n\n- - -\n\n"
            } else {
                texts = "ERROR"
            }
          
            if let dataObfuscated = try? encoder.encode(requestObfuscated),
               let jsonObfuscated = String(data: dataObfuscated, encoding: .utf8)  {
                textObfuscated = jsonObfuscated + "\n\n- - -\n\n"
            } else {
                textObfuscated = "ERROR"
            }
        } else if let customerValidateIdFaceMatchRequest = customerValidateIdFaceMatchRequest {
            // make a copy of the request and stub out the base64 image texts for logging
            let request = customerValidateIdFaceMatchRequest
          
            var requestObfuscated = request
            requestObfuscated.customerData.idData.idImageFront = "..."
            if requestObfuscated.customerData.idData.idImageBack != nil {
              requestObfuscated.customerData.idData.idImageBack = "..."
            }
            requestObfuscated.customerData.biometericData.selfie = "..."
          
            if let data = try? encoder.encode(request),
               let json = String(data: data, encoding: .utf8)  {
              texts = json + "\n\n- - -\n\n"
            } else {
                texts = "ERROR"
            }
          
            if let dataObfuscated = try? encoder.encode(requestObfuscated),
               let jsonObfuscated = String(data: dataObfuscated, encoding: .utf8)  {
               textObfuscated = jsonObfuscated + "\n\n- - -\n\n"
            } else {
               textObfuscated = "ERROR"
            }
        } else if let customerEnrollRequest = customerEnrollRequest {
            // make a copy of the request and stub out the base64 image text for logging
            let request = customerEnrollRequest
          
            var requestObfuscated = request
            requestObfuscated.customerData.idData.idImageFront = "..."
            if requestObfuscated.customerData.idData.idImageBack != nil {
              requestObfuscated.customerData.idData.idImageBack = "..."
            }
            requestObfuscated.customerData.biometericData.selfie = "..."
          
            if let data = try? encoder.encode(request),
               let json = String(data: data, encoding: .utf8)  {
                texts = json + "\n\n- - -\n\n"
            } else {
                texts = "ERROR"
            }
          
            if let dataObfuscated = try? encoder.encode(requestObfuscated),
               let jsonObfuscated = String(data: dataObfuscated, encoding: .utf8)  {
                textObfuscated = jsonObfuscated + "\n\n- - -\n\n"
            } else {
                textObfuscated = "ERROR"
            }
        } else if let customerEnrollBiometricsRequest = customerEnrollBiometricsRequest {
            // make a copy of the request and stub out the base64 image text for logging
            let request = customerEnrollBiometricsRequest
          
            var requestObfuscated = request
            requestObfuscated.customerData.biometericData.selfie = "..."
          
            if let data = try? encoder.encode(request),
               let json = String(data: data, encoding: .utf8)  {
                texts = json
            } else {
                texts = "ERROR"
            }
          
            if let dataObfuscated = try? encoder.encode(requestObfuscated),
               let jsonObfuscated = String(data: dataObfuscated, encoding: .utf8)  {
                textObfuscated = jsonObfuscated
            } else {
                textObfuscated = "ERROR"
            }
        } else if let customerVerifyRequest = customerVerifyRequest {
            // make a copy of the request and stub out the base64 image text for logging
            let request = customerVerifyRequest
          
            var requestObfuscated = request
            requestObfuscated.customerData.biometericData.selfie = "..."
          
            if let data = try? encoder.encode(request),
               let json = String(data: data, encoding: .utf8)  {
                texts = json
            } else {
                texts = "ERROR"
            }
          
            if let dataObfuscated = try? encoder.encode(requestObfuscated),
               let jsonObfuscated = String(data: dataObfuscated, encoding: .utf8)  {
                textObfuscated = jsonObfuscated
            } else {
                textObfuscated = "ERROR"
            }
        } else if let customerIdentifyRequest = customerIdentifyRequest {
            // make a copy of the request and stub out the base64 image text for logging
            let request = customerIdentifyRequest
          
            var requestObfuscated = request
            requestObfuscated.biometericData.selfie = "..."
          
            if let data = try? encoder.encode(request),
               let json = String(data: data, encoding: .utf8)  {
                texts = json
            } else {
                texts = "ERROR"
            }
          
            if let dataObfuscated = try? encoder.encode(requestObfuscated),
               let jsonObfuscated = String(data: dataObfuscated, encoding: .utf8)  {
                textObfuscated = jsonObfuscated
            } else {
                textObfuscated = "ERROR"
            }
        } else if let customerLiveCheckRequest = customerLiveCheckRequest {
            // make a copy of the request and stub out the base64 image text for logging
            let request = customerLiveCheckRequest
          
            var requestObfuscated = request
            requestObfuscated.customerData.biometericData.selfie = "..."
          
            if let data = try? encoder.encode(request),
               let json = String(data: data, encoding: .utf8)  {
                texts = json
            } else {
                texts = "ERROR"
            }
          
            if let dataObfuscated = try? encoder.encode(requestObfuscated),
               let jsonObfuscated = String(data: dataObfuscated, encoding: .utf8)  {
                textObfuscated = jsonObfuscated
            } else {
                textObfuscated = "ERROR"
            }
        }

        guard let frontDetectedData = frontDetectedData else { return }
        
        display(detectedData: frontDetectedData, type: frontDetectedData.type ?? "UNKNOWN")
        if let backDetectedData = backDetectedData {
            texts += "\n\n"
            display(detectedData: backDetectedData, type: backDetectedData.type ?? "UNKNOWN")
        } else {
            texts += "\n"
        }
      
        displayObfuscated(detectedData: frontDetectedData, type: frontDetectedData.type ?? "UNKNOWN")
        if let backDetectedData = backDetectedData {
            textObfuscated += "\n\n"
            displayObfuscated(detectedData: backDetectedData, type: backDetectedData.type ?? "UNKNOWN")
        } else {
            textObfuscated += "\n"
        }
      
        guard let faceMatch = faceMatch else { return }
        texts += "\n- - -\n\n"
        texts += "Face Match Distance: \(faceMatch.distance)\n"
        texts += "Face Matched?: \(faceMatch.isMatched ? "YES" : "NO")"
        textObfuscated += "\n- - -\n\n"
        textObfuscated += "Face Match Distance: \(faceMatch.distance)\n"
        textObfuscated += "Face Matched?: \(faceMatch.isMatched ? "YES" : "NO")"
    }

  override func viewWillAppear(_ animated: Bool) {
          super.viewWillAppear(animated)
          navigationController?.setNavigationBarHidden(false, animated: true)
          navigationItem.setHidesBackButton(true, animated: true)

          navigationItem.leftBarButtonItem = UIBarButtonItem(title: "Submit", style: .plain, target: self, action: #selector(submit(sender:)))
    
           var obfuscatedText = textObfuscated
           obfuscatedText = obfuscatedText?.replacingOccurrences(of: UserDefaults.defaultPassword, with: "", options: .literal, range: nil)

           let alertController = UIAlertController(title: "Extracted Data", message: obfuscatedText, preferredStyle: .alert)
           let OKAction = UIAlertAction(title: "OK", style: .cancel) { (action) in
               alertController.dismiss(animated: true, completion: nil)
           }
           alertController.addAction(OKAction)

           let paragraphStyle = NSMutableParagraphStyle()
           paragraphStyle.alignment = NSTextAlignment.left

           let messageText = NSMutableAttributedString(
               string: obfuscatedText ?? "Unable to obfuscate",
               attributes: [
                NSAttributedString.Key.paragraphStyle: paragraphStyle,
                NSAttributedString.Key.font: UIFont.systemFont(ofSize: 13.0)
               ]
           )

           alertController.setValue(messageText, forKey: "attributedMessage")
           self.present(alertController, animated: true, completion: nil)
      }
  
    @objc func submit(sender: Any) {
        let spinner: UIActivityIndicatorView
        if #available(iOS 13, *) {
            spinner = UIActivityIndicatorView(style: .medium)
        } else {
            spinner = UIActivityIndicatorView(style: .gray)
        }
        spinner.startAnimating()
        navigationItem.leftBarButtonItem = UIBarButtonItem(customView: spinner)

        if let request = customerValidateIdRequest {
            IDentitySDK.submit(customerValidateIdRequest: request) { result in
                self.navigationItem.leftBarButtonItem = nil

                switch result {
                case .success(let response):
                    let encoder = JSONEncoder()
                    encoder.outputFormatting = .prettyPrinted
                    if let data = try? encoder.encode(response), let json = String(data: data, encoding: .utf8) {
                        self.texts = json + "\n\n- - -\n\n" + self.texts
                    }
                    self.sendData()
                    self.dismiss()
                case .failure(let error):
                    self.texts = error.localizedDescription + "\n\n- - -\n\n" + self.texts
                    self.sendData()
                    self.dismiss()
                }
            }
        } else if let request = customerValidateIdFaceMatchRequest {
            IDentitySDK.submit(customerValidateIdFaceMatchRequest: request) { result in
                self.navigationItem.leftBarButtonItem = nil

                switch result {
                case .success(let response):
                    let encoder = JSONEncoder()
                    encoder.outputFormatting = .prettyPrinted
                    if let data = try? encoder.encode(response), let json = String(data: data, encoding: .utf8) {
                        self.texts = json + "\n\n- - -\n\n" + self.texts
                    }
                    self.sendData()
                    self.dismiss()
                case .failure(let error):
                    self.texts = error.localizedDescription + "\n\n- - -\n\n" + self.texts
                    self.sendData()
                    self.dismiss()
                }
            }
        } else if let request = customerEnrollRequest {
            IDentitySDK.submit(customerEnrollRequest: request) { result in
                self.navigationItem.leftBarButtonItem = nil

                switch result {
                case .success(let response):
                    let encoder = JSONEncoder()
                    encoder.outputFormatting = .prettyPrinted
                    if let data = try? encoder.encode(response), let json = String(data: data, encoding: .utf8) {
                        self.texts = json + "\n\n- - -\n\n" + self.texts
                    }
                    self.sendData()
                    self.dismiss()
                case .failure(let error):
                    self.texts = error.localizedDescription + "\n\n- - -\n\n" + self.texts
                    self.sendData()
                    self.dismiss()
                }
            }
        } else if let request = customerEnrollBiometricsRequest {
            IDentitySDK.submit(customerEnrollBiometricsRequest: request) { result in
                self.navigationItem.leftBarButtonItem = nil

                switch result {
                case .success(let response):
                    let encoder = JSONEncoder()
                    encoder.outputFormatting = .prettyPrinted
                    if let data = try? encoder.encode(response), let json = String(data: data, encoding: .utf8) {
                        self.texts = json + "\n\n- - -\n\n" + self.texts
                    }
                    self.sendData()
                    self.dismiss()
                case .failure(let error):
                    self.texts = error.localizedDescription + "\n\n- - -\n\n" + self.texts
                    self.sendData()
                    self.dismiss()
                }
            }
        } else if let request = customerVerifyRequest {
            IDentitySDK.submit(customerVerifyRequest: request) { result in
                self.navigationItem.leftBarButtonItem = nil

                switch result {
                case .success(let response):
                    let encoder = JSONEncoder()
                    encoder.outputFormatting = .prettyPrinted
                    if let data = try? encoder.encode(response), let json = String(data: data, encoding: .utf8) {
                        self.texts = json + "\n\n- - -\n\n" + self.texts
                    }
                    self.sendData()
                    self.dismiss()
                case .failure(let error):
                    self.texts = error.localizedDescription + "\n\n- - -\n\n" + self.texts
                    self.sendData()
                    self.dismiss()
                }
            }
        } else if let request = customerIdentifyRequest {
            IDentitySDK.submit(customerIdentifyRequest: request) { result in
                self.navigationItem.leftBarButtonItem = nil

                switch result {
                case .success(let response):
                    let encoder = JSONEncoder()
                    encoder.outputFormatting = .prettyPrinted
                    if let data = try? encoder.encode(response), let json = String(data: data, encoding: .utf8) {
                        self.texts = json + "\n\n- - -\n\n" + self.texts
                    }
                    self.sendData()
                    self.dismiss()
                case .failure(let error):
                    self.texts = error.localizedDescription + "\n\n- - -\n\n" + self.texts
                    self.sendData()
                    self.dismiss()
                }
            }
        } else if let request = customerLiveCheckRequest {
            IDentitySDK.submit(customerLiveCheckRequest: request) { result in
                self.navigationItem.leftBarButtonItem = nil

                switch result {
                case .success(let response):
                    let encoder = JSONEncoder()
                    encoder.outputFormatting = .prettyPrinted
                    if let data = try? encoder.encode(response), let json = String(data: data, encoding: .utf8) {
                        self.texts = json + "\n\n- - -\n\n" + self.texts
                    }
                    self.sendData()
                    self.dismiss()
                case .failure(let error):
                    self.texts = error.localizedDescription + "\n\n- - -\n\n" + self.texts
                    self.sendData()
                    self.dismiss()
                }
            }
        }
    }

  private func sendData() {
    let dict2:NSMutableDictionary? = ["data" : self.texts ?? ["data" : "error"]]
    let iDMissionSDK = IDMissionSDK()
    iDMissionSDK.getEvent2("DataCallback", dict: dict2 ?? ["data" : "error"])
  }
  
  private func display(detectedData data: DetectedData, type: String) {
        texts += type + "\n\n"

        texts += "Photo Present: "
        texts += data.shouldHavePhoto ? (data.isPhotoPresent ? "YES" : "NO") : "N/A"
        texts += "\n\n"

        texts += "MRZ Present: "
        if data.shouldHaveMrz {
            let dateFormatter = DateFormatter()
            dateFormatter.dateStyle = .short
            dateFormatter.timeZone = TimeZone(secondsFromGMT: 0)
            let encoder = JSONEncoder()
            encoder.outputFormatting = .prettyPrinted
            encoder.dateEncodingStrategy = .formatted(dateFormatter)
            if let mrz = data.mrz,
               let jsonData = try? encoder.encode(mrz),
               let json = String(data: jsonData, encoding: .utf8) {
               texts += "\n\n"
               texts += json
            } else {
               texts += "NO"
            }
        } else {
           texts += "N/A"
        }
        texts += "\n\n"

        texts += "Barcode Present: "
        if data.shouldHaveBarcode {
            if let barcode = data.barcode {
               texts += "\n\n"
                for key in barcode.keys {
                    if let value = barcode[key] {
                      texts += "\(key): \(value)\n"
                    }
                }
            } else {
               texts += "NO"
            }
        } else {
           texts += "N/A"
        }
    }

  private func displayObfuscated(detectedData data: DetectedData, type: String) {
        textObfuscated += type + "\n\n"

        textObfuscated += "Photo Present: "
        textObfuscated += data.shouldHavePhoto ? (data.isPhotoPresent ? "YES" : "NO") : "N/A"
        textObfuscated += "\n\n"

        textObfuscated += "MRZ Present: "
        if data.shouldHaveMrz {
            let dateFormatter = DateFormatter()
            dateFormatter.dateStyle = .short
            dateFormatter.timeZone = TimeZone(secondsFromGMT: 0)
            let encoder = JSONEncoder()
            encoder.outputFormatting = .prettyPrinted
            encoder.dateEncodingStrategy = .formatted(dateFormatter)
            if let mrz = data.mrz,
               let jsonData = try? encoder.encode(mrz),
               let json = String(data: jsonData, encoding: .utf8) {
                textObfuscated += "\n\n"
                textObfuscated += json
            } else {
                textObfuscated += "NO"
            }
        } else {
            textObfuscated += "N/A"
        }
        textObfuscated += "\n\n"

        textObfuscated += "Barcode Present: "
        if data.shouldHaveBarcode {
            if let barcode = data.barcode {
                textObfuscated += "\n\n"
                for key in barcode.keys {
                    if let value = barcode[key] {
                      textObfuscated += "\(key): \(value)\n"
                    }
                }
            } else {
                textObfuscated += "NO"
            }
        } else {
            textObfuscated += "N/A"
        }
    }
  
    func dismiss() {
        dismiss(animated: true, completion: nil)
    }
}
