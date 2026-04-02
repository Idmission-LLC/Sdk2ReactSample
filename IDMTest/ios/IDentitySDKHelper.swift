import Foundation
import IDentityMediumSDK
import SelfieCaptureMedium
import IDCaptureMedium

class IDentitySDKHelper : NSObject{
  
  @IBAction func initializeSDK(data: NSDictionary, instances: UIViewController) {
    
    let apiBaseUrl:String = data["apiBaseUrl"] as! String
    let authUrl:String = data["authUrl"] as! String
    let debug:String = data["debug"] as! String
    let accessToken:String = data["accessToken"] as! String
    
    IDentitySDK.apiBaseUrl = UserDefaults.apiBaseURL
    IDentitySDK.initializeSDK(language: UserDefaults.SDKlanguage, isUpdateModelsData: UserDefaults.isUpdateModelData, accessToken: UserDefaults.accessToken) { error in
        if let error = error {
            print("!!! initialize SDK ERROR: \(error.localizedDescription)")
        } else {
            print("!!! initialize SDK SUCCESS")
            self.sendData(text: "SDK successfully initialized")
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
  
  @IBAction func submitResult(instances: UIViewController) {
    ViewController().submitResult(instance: instances);
  }
  
  private func sendData(text: String) {
    let dict2:NSMutableDictionary? = ["data" : text ]
    let iDMissionSDK = IDMissionSDK()
    iDMissionSDK.getEvent2("DataCallback", dict: dict2 ?? ["data" : "error"])
  }
}
