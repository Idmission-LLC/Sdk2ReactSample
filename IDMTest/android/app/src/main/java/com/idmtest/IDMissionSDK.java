package com.idmtest;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Parcelable;

import androidx.annotation.Nullable;
import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.idmission.sdk2.capture.IdMissionCaptureLauncher;
import com.idmission.sdk2.capture.presentation.camera.helpers.ProcessedCapture;
import com.idmission.sdk2.client.model.AdditionalCustomerLiveCheckResponseData;
import com.idmission.sdk2.client.model.AliasesResponse;
import com.idmission.sdk2.client.model.CommonApiResponse;
import com.idmission.sdk2.client.model.CriminalRecordResponse;
import com.idmission.sdk2.client.model.ExtractedIdData;
import com.idmission.sdk2.client.model.ExtractedPersonalData;
import com.idmission.sdk2.client.model.HostDataResponse;
import com.idmission.sdk2.client.model.InitializeResponse;
import com.idmission.sdk2.client.model.NmResultResponse;
import com.idmission.sdk2.client.model.OffensesResponse;
import com.idmission.sdk2.client.model.PepResultResponse;
import com.idmission.sdk2.client.model.ProfilesResponse;
import com.idmission.sdk2.client.model.Response;
import com.idmission.sdk2.client.model.ResponseCustomerData;
import com.idmission.sdk2.client.model.ResultData;
import com.idmission.sdk2.client.model.SexOffendersResponse;
import com.idmission.sdk2.client.model.Status;
import com.idmission.sdk2.client.model.TextMatchResultResponse;
import com.idmission.sdk2.client.model.WlsResultResponse;
import com.idmission.sdk2.identityproofing.IdentityProofingSDK;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class IDMissionSDK extends ReactContextBaseJavaModule implements ActivityEventListener {
    String InitializeApiBaseUrl = "https://demo.idmission.com/";
    String ApiBaseUrl = "https://apidemo.idmission.com/";
    String LoginID = "";
    String Password = "";
    long MerchantID = 0;
    private List<ProcessedCapture> processedCaptures = new ArrayList<>();
    private IdMissionCaptureLauncher launcher = new IdMissionCaptureLauncher();
    ReactApplicationContext reactContext;

    //constructor
    public IDMissionSDK(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
        this.reactContext.addActivityEventListener(this);
    }

    @Override
    public String getName() {
        return "IDMissionSDK";
    }

    @ReactMethod
    public void  initializeSDK(String initializeURL, String url, String loginID, String password, String merchantID) {
        if(!StringUtils.isEmpty(initializeURL)){
            InitializeApiBaseUrl = initializeURL;
        }
        if(!StringUtils.isEmpty(url)){
            ApiBaseUrl = url;
        }
        if(!StringUtils.isEmpty(loginID)){
            LoginID = loginID;
        }
        if(!StringUtils.isEmpty(password)){
            Password = password;
        }
        if(!StringUtils.isEmpty(merchantID)){
            MerchantID = Long.parseLong(merchantID);
        }
        new BackgroundTask().execute();
    }

    @ReactMethod
    public void  serviceID20() {
        IdentityProofingSDK.INSTANCE.idValidation(getReactApplicationContext().getCurrentActivity());
    }

    @ReactMethod
    public void  serviceID10() {
        IdentityProofingSDK.INSTANCE.idValidationAndMatchFace(getReactApplicationContext().getCurrentActivity());
    }

    @ReactMethod
    public void  serviceID185() {
        IdentityProofingSDK.INSTANCE.identifyCustomer(getReactApplicationContext().getCurrentActivity(), null, null);
    }

    @ReactMethod
    public void  serviceID660() {
        IdentityProofingSDK.INSTANCE.liveFaceCheck(getReactApplicationContext().getCurrentActivity());
    }

    @ReactMethod
    public void  serviceID50(String uniqueCustomerNumber) {
        if(!StringUtils.isEmpty(uniqueCustomerNumber)){
            IdentityProofingSDK.INSTANCE.idValidationAndcustomerEnroll(getReactApplicationContext().getCurrentActivity(), uniqueCustomerNumber);
        }
    }

    @ReactMethod
    public void  serviceID175(String uniqueCustomerNumber) {
        if(!StringUtils.isEmpty(uniqueCustomerNumber)){
            IdentityProofingSDK.INSTANCE.customerEnrollBiometrics(getReactApplicationContext().getCurrentActivity(), uniqueCustomerNumber);
        }
    }

    @ReactMethod
    public void  serviceID105(String uniqueCustomerNumber) {
        if(!StringUtils.isEmpty(uniqueCustomerNumber)){
            IdentityProofingSDK.INSTANCE.customerVerification(getReactApplicationContext().getCurrentActivity(), uniqueCustomerNumber);
        }
    }

    @ReactMethod
    public void submitResult(){
        new FinalSubmitTask().execute();
    }

    class BackgroundTask extends AsyncTask<Void, Void, Response<InitializeResponse>> {

        @Override
        protected void onPostExecute(Response<InitializeResponse> initializeResponseResponse) {
            super.onPostExecute(initializeResponseResponse);
            WritableMap params = Arguments.createMap();
            params.putString("data",initializeResponseResponse.getResult().toString());
            sendEvent(getReactApplicationContext(), "DataCallback", params);
        }

        @Override
        protected Response<InitializeResponse> doInBackground(Void... voids) {
            Response<InitializeResponse> response =
                    IdentityProofingSDK.INSTANCE.initialize(getReactApplicationContext().getCurrentActivity(),
                            InitializeApiBaseUrl,
                            ApiBaseUrl,
                            LoginID,
                            Password,
                            MerchantID,
                            false,
                            true);
            return response;
        }
    }

    class FinalSubmitTask extends AsyncTask<Void, Void, Response<CommonApiResponse>> {

        @Override
        protected void onPostExecute(Response<CommonApiResponse> apiResponse) {
            super.onPostExecute(apiResponse);
            if(apiResponse.getErrorStatus()!=null) {
                WritableMap params = Arguments.createMap();
                params.putString("data",apiResponse.getErrorStatus().getStatusMessage());
                sendEvent(getReactApplicationContext(), "DataCallback", params);
            } else  {
                WritableMap params = Arguments.createMap();
                params.putString("data",parseResponse(apiResponse));
                sendEvent(getReactApplicationContext(), "DataCallback", params);
            }
        }

        @Override
        protected Response<CommonApiResponse> doInBackground(Void... voids) {

            Response<CommonApiResponse> response =
                    IdentityProofingSDK.INSTANCE.finalSubmit(getReactApplicationContext().getCurrentActivity());
            return response;
        }
    }

    @Override
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        if (data != null) {
            if (requestCode == IdMissionCaptureLauncher.CAPTURE_REQUEST_CODE) {
                try{
                    Parcelable[] processedCaptures = data.getExtras().getParcelableArray(IdMissionCaptureLauncher.EXTRA_PROCESSED_CAPTURES);

                    JSONObject jo = new JSONObject();

                    for (Parcelable pc : processedCaptures)
                    {
                        if (pc instanceof ProcessedCapture.DocumentDetectionResult.RealDocument) {
                            try {
                                ProcessedCapture.DocumentDetectionResult.RealDocument rd = (ProcessedCapture.DocumentDetectionResult.RealDocument) pc;

                                JSONObject realDocumentData = new JSONObject();
                                realDocumentData.put("barcodeMap",rd.getBarcodeMap());
                                realDocumentData.put("barcodeString",rd.getBarcodeString());
                                realDocumentData.put("mrzMap",rd.getMrzMap());
                                realDocumentData.put("confidenceScore",rd.getConfidenceScore());
                                realDocumentData.put("detectedRect",rd.getDetectedRect());
                                realDocumentData.put("faceMatch",rd.getFaceMatch());
                                realDocumentData.put("faceOnId",rd.getFaceOnId());
                                realDocumentData.put("file",rd.getFile());
                                realDocumentData.put("mrzString",rd.getMrzString());
                                realDocumentData.put("ocrString",rd.getOcrString());
                                realDocumentData.put("operation",rd.getOperation());
                                realDocumentData.put("realnessScore",rd.getRealnessScore());
                                realDocumentData.put("timeDetectedAt",rd.getTimeDetectedAt());
                                realDocumentData.put("timeFinishedAt",rd.getTimeFinishedAt());
                                realDocumentData.put("timeStartedAt",rd.getTimeStartedAt());
                                realDocumentData.put("timeWithinBoundsAt",rd.getTimeWithinBoundsAt());
                                realDocumentData.put("modelName",rd.getModelName());

                                jo.put("realDocument",realDocumentData);
                            }catch(Exception e){}
                        }else if (pc instanceof ProcessedCapture.DocumentDetectionResult.SpoofDocument) {
                            try {
                                ProcessedCapture.DocumentDetectionResult.SpoofDocument sd = (ProcessedCapture.DocumentDetectionResult.SpoofDocument) pc;

                                JSONObject spoofDocumentData = new JSONObject();
                                spoofDocumentData.put("confidenceScore",sd.getConfidenceScore());
                                spoofDocumentData.put("detectedRect",sd.getDetectedRect());
                                spoofDocumentData.put("operation",sd.getOperation());
                                spoofDocumentData.put("realnessScore",sd.getRealnessScore());
                                spoofDocumentData.put("timeDetectedAt",sd.getTimeDetectedAt());
                                spoofDocumentData.put("timeFinishedAt",sd.getTimeFinishedAt());
                                spoofDocumentData.put("timeStartedAt",sd.getTimeStartedAt());
                                spoofDocumentData.put("timeWithinBoundsAt",sd.getTimeWithinBoundsAt());
                                spoofDocumentData.put("modelName",sd.getModelName());

                                jo.put("spoofDocument",spoofDocumentData);
                            }catch(Exception e){}
                        }else if (pc instanceof ProcessedCapture.LiveFaceDetectionResult.RealFace) {
                            try {
                                ProcessedCapture.LiveFaceDetectionResult.RealFace rf = (ProcessedCapture.LiveFaceDetectionResult.RealFace) pc;

                                JSONObject realFaceData = new JSONObject();
                                realFaceData.put("detectedRect",rf.getDetectedRect());
                                realFaceData.put("operation",rf.getOperation());
                                realFaceData.put("timeDetectedAt",rf.getTimeDetectedAt());
                                realFaceData.put("timeFinishedAt",rf.getTimeFinishedAt());
                                realFaceData.put("timeStartedAt",rf.getTimeStartedAt());
                                realFaceData.put("timeWithinBoundsAt",rf.getTimeWithinBoundsAt());
                                realFaceData.put("faceMatch",rf.getFaceMatch());
                                realFaceData.put("file",rf.getFile());
                                realFaceData.put("livenessScore",rf.getLivenessScore());

                                jo.put("realFace",realFaceData);
                            }catch(Exception e){}
                        }else if (pc instanceof ProcessedCapture.LiveFaceDetectionResult.SpoofFace) {
                            try {
                                ProcessedCapture.LiveFaceDetectionResult.SpoofFace sf = (ProcessedCapture.LiveFaceDetectionResult.SpoofFace) pc;

                                JSONObject spoofFaceData = new JSONObject();
                                spoofFaceData.put("detectedRect",sf.getDetectedRect());
                                spoofFaceData.put("operation",sf.getOperation());
                                spoofFaceData.put("timeDetectedAt",sf.getTimeDetectedAt());
                                spoofFaceData.put("timeFinishedAt",sf.getTimeFinishedAt());
                                spoofFaceData.put("timeStartedAt",sf.getTimeStartedAt());
                                spoofFaceData.put("timeWithinBoundsAt",sf.getTimeWithinBoundsAt());
                                spoofFaceData.put("livenessScore",sf.getLivenessScore());

                                jo.put("spoofFace",spoofFaceData);
                            }catch(Exception e){}
                        }
                    }

                    WritableMap params = Arguments.createMap();
                    params.putString("data",jo.toString());
                    sendEvent(getReactApplicationContext(), "DataCallback", params);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onNewIntent(Intent intent) {}

    private void sendEvent(ReactApplicationContext reactContext,
                           String eventName,
                           @Nullable WritableMap params) {
        reactContext
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(eventName, params);
    }

    private String parseResponse(Response<CommonApiResponse> response){
        JSONObject jo = new JSONObject();
        try {
            JSONObject statusData = new JSONObject();

            Status status = response.getResult().getStatus();
            statusData.put("statusCode",status.getStatusCode());
            statusData.put("requestId",status.getRequestId());
            statusData.put("errorData",status.getErrorData());
            statusData.put("statusMessage",status.getStatusMessage());

            jo.put("status", statusData);
        }catch(Exception e){}

        try {
            JSONObject resultData = new JSONObject();

            ResultData rd = response.getResult().getResultData();
            resultData.put("uniqueRequestId",rd.getUniqueRequestId());
            resultData.put("verificationResultCode",rd.getVerificationResultCode());
            resultData.put("verificationResultId",rd.getVerificationResultId());
            resultData.put("verificationResult",rd.getVerificationResult());

            jo.put("resultData", resultData);
        }catch(Exception e){}

        try {
            JSONObject responseCustomerData = new JSONObject();
            JSONObject extractedIdData = new JSONObject();
            JSONObject extractedPersonalData = new JSONObject();
            JSONObject hostDataResponseData = new JSONObject();
            JSONObject criminalRecordResponseData = new JSONObject();
            JSONObject aliasesResponseData = new JSONObject();
            JSONObject offensesResponseData = new JSONObject();
            JSONObject profilesResponseData = new JSONObject();
            JSONObject nmResultResponseResponseData = new JSONObject();
            JSONObject pepResultResponseData = new JSONObject();
            JSONObject textMatchResultResponseData = new JSONObject();
            JSONObject sexOffendersResponseData = new JSONObject();
            JSONObject profilesResponse2Data = new JSONObject();
            JSONObject wlsResultResponseData = new JSONObject();

            ResponseCustomerData rcd = response.getResult().getResponseCustomerData();

            try {
                ExtractedIdData eid = rcd.getExtractedIdData();
                extractedIdData.put("barcodeDataParsed",eid.getBarcodeDataParsed());
                extractedIdData.put("idCountry",eid.getIdCountry());
                extractedIdData.put("idDateOfBirth",eid.getIdDateOfBirth());
                extractedIdData.put("idDateOfBirthFormatted",eid.getIdDateOfBirthFormatted());
                extractedIdData.put("idDateOfBirthNonEng",eid.getIdDateOfBirthNonEng());
                extractedIdData.put("idExpirationDate",eid.getIdExpirationDate());
                extractedIdData.put("idExpirationDateFormatted",eid.getIdExpirationDateFormatted());
                extractedIdData.put("idExpirationDateNonEng",eid.getIdExpirationDateNonEng());
                extractedIdData.put("idIssueCountry",eid.getIdIssueCountry());
                extractedIdData.put("idIssueDate",eid.getIdIssueDate());
                extractedIdData.put("idIssueDateNonEng",eid.getIdIssueDateNonEng());
                extractedIdData.put("idNumber",eid.getIdNumber());
                extractedIdData.put("idNumberNonEng",eid.getIdNumberNonEng());
                extractedIdData.put("idNumber1",eid.getIdNumber1());
                extractedIdData.put("idNumber2",eid.getIdNumber2());
                extractedIdData.put("idNumber2NonEng",eid.getIdNumber2NonEng());
                extractedIdData.put("idNumber3",eid.getIdNumber3());
                extractedIdData.put("idState",eid.getIdState());
                extractedIdData.put("idType",eid.getIdType());
                extractedIdData.put("mrzData",eid.getMrzData());
                responseCustomerData.put("extractedIdData",extractedIdData);
            }catch(Exception e){}

            try {
                ExtractedPersonalData epd = rcd.getExtractedPersonalData();
                extractedPersonalData.put("addressLine1",epd.getAddressLine1());
                extractedPersonalData.put("addressLine1NonEng",epd.getAddressLine1NonEng());
                extractedPersonalData.put("addressLine2",epd.getAddressLine2());
                extractedPersonalData.put("addressLine2NonEng",epd.getAddressLine2NonEng());
                extractedPersonalData.put("city",epd.getCity());
                extractedPersonalData.put("addressNonEng",epd.getAddressNonEng());
                extractedPersonalData.put("country",epd.getCountry());
                extractedPersonalData.put("district",epd.getDistrict());
                extractedPersonalData.put("dob",epd.getDob());
                extractedPersonalData.put("email",epd.getEmail());
                extractedPersonalData.put("enrolledDate",epd.getEnrolledDate());
                extractedPersonalData.put("firstName",epd.getFirstName());
                extractedPersonalData.put("firstNameNonEng",epd.getFirstNameNonEng());
                extractedPersonalData.put("gender",epd.getGender());
                extractedPersonalData.put("lastName",epd.getLastName());
                extractedPersonalData.put("lastName2",epd.getLastName2());
                extractedPersonalData.put("lastNameNonEng",epd.getLastNameNonEng());
                extractedPersonalData.put("name",epd.getName());
                extractedPersonalData.put("phone",epd.getPhone());
                extractedPersonalData.put("uniqueNumber",epd.getUniqueNumber());
                extractedPersonalData.put("middleName",epd.getMiddleName());
                extractedPersonalData.put("middleNameNonEng",epd.getMiddleNameNonEng());
                responseCustomerData.put("extractedPersonalData",extractedPersonalData);
            }catch(Exception e){}

            try {
                HostDataResponse hdr = rcd.getHostData();

                try {
                    CriminalRecordResponse crr = hdr.getCriminalRecord();

                    try {
                        AliasesResponse ar = crr.getAliasesResponse();
                        aliasesResponseData.put("firstName",ar.getFirstName());
                        aliasesResponseData.put("middleName",ar.getMiddleName());
                        aliasesResponseData.put("lastName",ar.getLastName());
                        aliasesResponseData.put("fullName",ar.getFullName());
                        criminalRecordResponseData.put("aliases",aliasesResponseData);
                    }catch(Exception e){}

                    try {
                        OffensesResponse or = crr.getOffensesResponse();
                        offensesResponseData.put("addmissionDate",or.getAddmissionDate());
                        offensesResponseData.put("ageOfVictim",or.getAgeOfVictim());
                        offensesResponseData.put("arrestingAgency",or.getArrestingAgency());
                        offensesResponseData.put("caseNumber",or.getCaseNumber());
                        offensesResponseData.put("category",or.getCategory());
                        offensesResponseData.put("chargeFillingDate",or.getChargeFillingDate());
                        offensesResponseData.put("closedDate",or.getClosedDate());
                        offensesResponseData.put("code",or.getCode());
                        offensesResponseData.put("counts",or.getCounts());
                        offensesResponseData.put("courts",or.getCourts());
                        offensesResponseData.put("dateConvicted",or.getDateConvicted());
                        offensesResponseData.put("dateOfCrime",or.getDateOfCrime());
                        offensesResponseData.put("dateOfWarrant",or.getDateOfWarrant());
                        offensesResponseData.put("description",or.getDescription());
                        offensesResponseData.put("dispositionDate",or.getDispositionDate());
                        offensesResponseData.put("dispostion",or.getDispostion());
                        offensesResponseData.put("facility",or.getFacility());
                        offensesResponseData.put("jurisdication",or.getJurisdication());
                        offensesResponseData.put("prisonerNumber",or.getPrisonerNumber());
                        offensesResponseData.put("relationshipToVictim",or.getRelationshipToVictim());
                        offensesResponseData.put("releaseDate",or.getReleaseDate());
                        offensesResponseData.put("section",or.getSection());
                        offensesResponseData.put("sentence",or.getSentence());
                        offensesResponseData.put("sentenceDate",or.getSentenceDate());
                        offensesResponseData.put("subsection",or.getSubsection());
                        offensesResponseData.put("title",or.getTitle());
                        offensesResponseData.put("warrantDate",or.getWarrantDate());
                        offensesResponseData.put("warrantNumber",or.getWarrantNumber());
                        offensesResponseData.put("weaponsUsed",or.getWeaponsUsed());
                        criminalRecordResponseData.put("offenses",offensesResponseData);
                    }catch(Exception e){}

                    try {
                        ProfilesResponse pr = crr.getProfiles();
                        profilesResponseData.put("city",pr.getCity());
                        profilesResponseData.put("country",pr.getCountry());
                        profilesResponseData.put("fullName",pr.getFullName());
                        profilesResponseData.put("firstName",pr.getFirstName());
                        profilesResponseData.put("middleName",pr.getMiddleName());
                        profilesResponseData.put("address",pr.getAddress());
                        profilesResponseData.put("convictionType",pr.getConvictionType());
                        profilesResponseData.put("countryCode",pr.getCountryCode());
                        profilesResponseData.put("countryName",pr.getCountryName());
                        profilesResponseData.put("dobOfBirth",pr.getDobOfBirth());
                        profilesResponseData.put("drivingLicenseVerificationResult",pr.getDrivingLicenseVerificationResult());
                        profilesResponseData.put("internalId",pr.getInternalId());
                        profilesResponseData.put("internalIdCriminalRecords",pr.getInternalIdCriminalRecords());
                        profilesResponseData.put("lastName",pr.getLastName());
                        profilesResponseData.put("photoUrl",pr.getPhotoUrl());
                        profilesResponseData.put("postalCode",pr.getPostalCode());
                        profilesResponseData.put("sex",pr.getSex());
                        profilesResponseData.put("source",pr.getSource());
                        profilesResponseData.put("state",pr.getState());
                        profilesResponseData.put("street1",pr.getStreet1());
                        profilesResponseData.put("street2",pr.getStreet2());
                        profilesResponseData.put("verificationResult",pr.getVerificationResult());
                        criminalRecordResponseData.put("profiles",profilesResponseData);
                    }catch(Exception e){}

                    hostDataResponseData.put("criminalRecord",criminalRecordResponseData);

                }catch(Exception e){}

                try {
                    NmResultResponse nrr = hdr.getNmresult();
                    nmResultResponseResponseData.put("createdOnNM",nrr.getCreatedOnNM());
                    nmResultResponseResponseData.put("orderIdNM",nrr.getOrderIdNM());
                    nmResultResponseResponseData.put("resultCountNM",nrr.getResultCountNM());
                    nmResultResponseResponseData.put("orderStatusNM",nrr.getOrderStatusNM());
                    nmResultResponseResponseData.put("orderUrlNM",nrr.getOrderUrlNM());
                    nmResultResponseResponseData.put("productIdNM",nrr.getProductIdNM());
                    nmResultResponseResponseData.put("vital4APIHostTried",nrr.getVital4APIHostTried());
                    hostDataResponseData.put("nmResult",nmResultResponseResponseData);
                }catch(Exception e){}

                try {
                    PepResultResponse prr = hdr.getPepresult();
                    pepResultResponseData.put("createdOnPEP",prr.getCreatedOnPEP());
                    pepResultResponseData.put("orderIdPEP",prr.getOrderIdPEP());
                    pepResultResponseData.put("resultCountPEP",prr.getResultCountPEP());
                    pepResultResponseData.put("productId_PEP",prr.getProductId_PEP());
                    pepResultResponseData.put("orderUrlPEP",prr.getOrderUrlPEP());
                    pepResultResponseData.put("orderStatus_PEP",prr.getOrderStatus_PEP());
                    hostDataResponseData.put("pepresult",pepResultResponseData);
                }catch(Exception e){}

                try {
                    TextMatchResultResponse tmrr = hdr.getTextMatchResult();
                    textMatchResultResponseData.put("address",tmrr.getAddress());
                    textMatchResultResponseData.put("addressCityMatch",tmrr.getAddressCityMatch());
                    textMatchResultResponseData.put("addressLine1Match",tmrr.getAddressLine1Match());
                    textMatchResultResponseData.put("addressLine2Match",tmrr.getAddressLine2Match());
                    textMatchResultResponseData.put("addressZIP4Match",tmrr.getAddressZIP4Match());
                    textMatchResultResponseData.put("addressStateCodeMatch",tmrr.getAddressStateCodeMatch());
                    textMatchResultResponseData.put("addressZIP5Match",tmrr.getAddressZIP5Match());
                    textMatchResultResponseData.put("documentCategoryMatch",tmrr.getDocumentCategoryMatch());
                    textMatchResultResponseData.put("driverLicenseExpirationDateMatch",tmrr.getDriverLicenseExpirationDateMatch());
                    textMatchResultResponseData.put("driverLicenseIssueDateMatch",tmrr.getDriverLicenseIssueDateMatch());
                    textMatchResultResponseData.put("driverLicenseNumberMatch",tmrr.getDriverLicenseNumberMatch());
                    textMatchResultResponseData.put("hostTried",tmrr.getHostTried());
                    textMatchResultResponseData.put("identiFraudHostTried",tmrr.getIdentiFraudHostTried());
                    textMatchResultResponseData.put("personBirthDateMatch",tmrr.getPersonBirthDateMatch());
                    textMatchResultResponseData.put("personFirstNameExactMatch",tmrr.getPersonFirstNameExactMatch());
                    textMatchResultResponseData.put("personFirstNameFuzzyAlternateMatch",tmrr.getPersonFirstNameFuzzyAlternateMatch());
                    textMatchResultResponseData.put("personFirstNameFuzzyPrimaryMatch",tmrr.getPersonFirstNameFuzzyPrimaryMatch());
                    textMatchResultResponseData.put("personLastNameExactMatch",tmrr.getPersonLastNameExactMatch());
                    textMatchResultResponseData.put("personLastNameFuzzyAlternateMatch",tmrr.getPersonLastNameFuzzyAlternateMatch());
                    textMatchResultResponseData.put("personLastNameFuzzyPrimaryMatch",tmrr.getPersonLastNameFuzzyPrimaryMatch());
                    textMatchResultResponseData.put("personMiddleInitialMatch",tmrr.getPersonMiddleInitialMatch());
                    textMatchResultResponseData.put("personMiddleNameExactMatch",tmrr.getPersonMiddleNameExactMatch());
                    textMatchResultResponseData.put("personMiddleNameFuzzyAlternateMatch",tmrr.getPersonMiddleNameFuzzyAlternateMatch());
                    textMatchResultResponseData.put("personMiddleNameFuzzyPrimaryMatch",tmrr.getPersonMiddleNameFuzzyPrimaryMatch());
                    textMatchResultResponseData.put("personSexCodeMatch",tmrr.getPersonSexCodeMatch());
                    textMatchResultResponseData.put("servicePresent",tmrr.getServicePresent());
                    textMatchResultResponseData.put("thirdPartyVerificationResultDescription",tmrr.getThirdPartyVerificationResultDescription());
                    textMatchResultResponseData.put("verificationResult",tmrr.getVerificationResult());
                    hostDataResponseData.put("textMatchResult",textMatchResultResponseData);
                }catch(Exception e){}

                try {
                    SexOffendersResponse sor = hdr.getSexOffenders();

                    try {
                        ProfilesResponse pr2 = sor.getProfiles();
                        profilesResponse2Data.put("city",pr2.getCity());
                        profilesResponse2Data.put("country",pr2.getCountry());
                        profilesResponse2Data.put("fullName",pr2.getFullName());
                        profilesResponse2Data.put("firstName",pr2.getFirstName());
                        profilesResponse2Data.put("middleName",pr2.getMiddleName());
                        profilesResponse2Data.put("address",pr2.getAddress());
                        profilesResponse2Data.put("convictionType",pr2.getConvictionType());
                        profilesResponse2Data.put("countryCode",pr2.getCountryCode());
                        profilesResponse2Data.put("countryName",pr2.getCountryName());
                        profilesResponse2Data.put("dobOfBirth",pr2.getDobOfBirth());
                        profilesResponse2Data.put("drivingLicenseVerificationResult",pr2.getDrivingLicenseVerificationResult());
                        profilesResponse2Data.put("internalId",pr2.getInternalId());
                        profilesResponse2Data.put("internalIdCriminalRecords",pr2.getInternalIdCriminalRecords());
                        profilesResponse2Data.put("lastName",pr2.getLastName());
                        profilesResponse2Data.put("photoUrl",pr2.getPhotoUrl());
                        profilesResponse2Data.put("postalCode",pr2.getPostalCode());
                        profilesResponse2Data.put("sex",pr2.getSex());
                        profilesResponse2Data.put("source",pr2.getSource());
                        profilesResponse2Data.put("state",pr2.getState());
                        profilesResponse2Data.put("street1",pr2.getStreet1());
                        profilesResponse2Data.put("street2",pr2.getStreet2());
                        profilesResponse2Data.put("verificationResult",pr2.getVerificationResult());
                        sexOffendersResponseData.put("profiles",profilesResponse2Data);
                    }catch(Exception e){}

                    hostDataResponseData.put("sexOffenders",sexOffendersResponseData);
                }catch(Exception e){}

                try {
                    WlsResultResponse wrr = hdr.getWlsresult();
                    wlsResultResponseData.put("createdOnWLS",wrr.getCreatedOnWLS());
                    wlsResultResponseData.put("orderIdWLS",wrr.getOrderIdWLS());
                    wlsResultResponseData.put("resultCountWLS",wrr.getResultCountWLS());
                    wlsResultResponseData.put("productIdWLS",wrr.getProductIdWLS());
                    wlsResultResponseData.put("orderStatusWLS",wrr.getOrderStatusWLS());
                    wlsResultResponseData.put("orderUrlWLS",wrr.getOrderUrlWLS());
                    hostDataResponseData.put("wlsresult",wlsResultResponseData);
                }catch(Exception e){}

                responseCustomerData.put("hostDataResponse",hostDataResponseData);
            }catch(Exception e){}

            jo.put("responseCustomerData", responseCustomerData);
        }catch(Exception e){}

        try {
            JSONObject responseCustomerVerifyData = new JSONObject();
            JSONObject extractedIdData = new JSONObject();
            JSONObject extractedPersonalData = new JSONObject();
            JSONObject hostDataResponseData = new JSONObject();
            JSONObject criminalRecordResponseData = new JSONObject();
            JSONObject aliasesResponseData = new JSONObject();
            JSONObject offensesResponseData = new JSONObject();
            JSONObject profilesResponseData = new JSONObject();
            JSONObject nmResultResponseResponseData = new JSONObject();
            JSONObject pepResultResponseData = new JSONObject();
            JSONObject textMatchResultResponseData = new JSONObject();
            JSONObject sexOffendersResponseData = new JSONObject();
            JSONObject profilesResponse2Data = new JSONObject();
            JSONObject wlsResultResponseData = new JSONObject();

            ResponseCustomerData rcvd = response.getResult().getResponseCustomerVerifyData();

            try {
                ExtractedIdData eid = rcvd.getExtractedIdData();
                extractedIdData.put("barcodeDataParsed",eid.getBarcodeDataParsed());
                extractedIdData.put("idCountry",eid.getIdCountry());
                extractedIdData.put("idDateOfBirth",eid.getIdDateOfBirth());
                extractedIdData.put("idDateOfBirthFormatted",eid.getIdDateOfBirthFormatted());
                extractedIdData.put("idDateOfBirthNonEng",eid.getIdDateOfBirthNonEng());
                extractedIdData.put("idExpirationDate",eid.getIdExpirationDate());
                extractedIdData.put("idExpirationDateFormatted",eid.getIdExpirationDateFormatted());
                extractedIdData.put("idExpirationDateNonEng",eid.getIdExpirationDateNonEng());
                extractedIdData.put("idIssueCountry",eid.getIdIssueCountry());
                extractedIdData.put("idIssueDate",eid.getIdIssueDate());
                extractedIdData.put("idIssueDateNonEng",eid.getIdIssueDateNonEng());
                extractedIdData.put("idNumber",eid.getIdNumber());
                extractedIdData.put("idNumberNonEng",eid.getIdNumberNonEng());
                extractedIdData.put("idNumber1",eid.getIdNumber1());
                extractedIdData.put("idNumber2",eid.getIdNumber2());
                extractedIdData.put("idNumber2NonEng",eid.getIdNumber2NonEng());
                extractedIdData.put("idNumber3",eid.getIdNumber3());
                extractedIdData.put("idState",eid.getIdState());
                extractedIdData.put("idType",eid.getIdType());
                extractedIdData.put("mrzData",eid.getMrzData());
                responseCustomerVerifyData.put("extractedIdData",extractedIdData);
            }catch(Exception e){}

            try {
                ExtractedPersonalData epd = rcvd.getExtractedPersonalData();
                extractedPersonalData.put("addressLine1",epd.getAddressLine1());
                extractedPersonalData.put("addressLine1NonEng",epd.getAddressLine1NonEng());
                extractedPersonalData.put("addressLine2",epd.getAddressLine2());
                extractedPersonalData.put("addressLine2NonEng",epd.getAddressLine2NonEng());
                extractedPersonalData.put("city",epd.getCity());
                extractedPersonalData.put("addressNonEng",epd.getAddressNonEng());
                extractedPersonalData.put("country",epd.getCountry());
                extractedPersonalData.put("district",epd.getDistrict());
                extractedPersonalData.put("dob",epd.getDob());
                extractedPersonalData.put("email",epd.getEmail());
                extractedPersonalData.put("enrolledDate",epd.getEnrolledDate());
                extractedPersonalData.put("firstName",epd.getFirstName());
                extractedPersonalData.put("firstNameNonEng",epd.getFirstNameNonEng());
                extractedPersonalData.put("gender",epd.getGender());
                extractedPersonalData.put("lastName",epd.getLastName());
                extractedPersonalData.put("lastName2",epd.getLastName2());
                extractedPersonalData.put("lastNameNonEng",epd.getLastNameNonEng());
                extractedPersonalData.put("name",epd.getName());
                extractedPersonalData.put("phone",epd.getPhone());
                extractedPersonalData.put("uniqueNumber",epd.getUniqueNumber());
                extractedPersonalData.put("middleName",epd.getMiddleName());
                extractedPersonalData.put("middleNameNonEng",epd.getMiddleNameNonEng());
                responseCustomerVerifyData.put("extractedPersonalData",extractedPersonalData);
            }catch(Exception e){}

            try {
                HostDataResponse hdr = rcvd.getHostData();

                try {
                    CriminalRecordResponse crr = hdr.getCriminalRecord();

                    try {
                        AliasesResponse ar = crr.getAliasesResponse();
                        aliasesResponseData.put("firstName",ar.getFirstName());
                        aliasesResponseData.put("middleName",ar.getMiddleName());
                        aliasesResponseData.put("lastName",ar.getLastName());
                        aliasesResponseData.put("fullName",ar.getFullName());
                        criminalRecordResponseData.put("aliases",aliasesResponseData);
                    }catch(Exception e){}

                    try {
                        OffensesResponse or = crr.getOffensesResponse();
                        offensesResponseData.put("addmissionDate",or.getAddmissionDate());
                        offensesResponseData.put("ageOfVictim",or.getAgeOfVictim());
                        offensesResponseData.put("arrestingAgency",or.getArrestingAgency());
                        offensesResponseData.put("caseNumber",or.getCaseNumber());
                        offensesResponseData.put("category",or.getCategory());
                        offensesResponseData.put("chargeFillingDate",or.getChargeFillingDate());
                        offensesResponseData.put("closedDate",or.getClosedDate());
                        offensesResponseData.put("code",or.getCode());
                        offensesResponseData.put("counts",or.getCounts());
                        offensesResponseData.put("courts",or.getCourts());
                        offensesResponseData.put("dateConvicted",or.getDateConvicted());
                        offensesResponseData.put("dateOfCrime",or.getDateOfCrime());
                        offensesResponseData.put("dateOfWarrant",or.getDateOfWarrant());
                        offensesResponseData.put("description",or.getDescription());
                        offensesResponseData.put("dispositionDate",or.getDispositionDate());
                        offensesResponseData.put("dispostion",or.getDispostion());
                        offensesResponseData.put("facility",or.getFacility());
                        offensesResponseData.put("jurisdication",or.getJurisdication());
                        offensesResponseData.put("prisonerNumber",or.getPrisonerNumber());
                        offensesResponseData.put("relationshipToVictim",or.getRelationshipToVictim());
                        offensesResponseData.put("releaseDate",or.getReleaseDate());
                        offensesResponseData.put("section",or.getSection());
                        offensesResponseData.put("sentence",or.getSentence());
                        offensesResponseData.put("sentenceDate",or.getSentenceDate());
                        offensesResponseData.put("subsection",or.getSubsection());
                        offensesResponseData.put("title",or.getTitle());
                        offensesResponseData.put("warrantDate",or.getWarrantDate());
                        offensesResponseData.put("warrantNumber",or.getWarrantNumber());
                        offensesResponseData.put("weaponsUsed",or.getWeaponsUsed());
                        criminalRecordResponseData.put("offenses",offensesResponseData);
                    }catch(Exception e){}

                    try {
                        ProfilesResponse pr = crr.getProfiles();
                        profilesResponseData.put("city",pr.getCity());
                        profilesResponseData.put("country",pr.getCountry());
                        profilesResponseData.put("fullName",pr.getFullName());
                        profilesResponseData.put("firstName",pr.getFirstName());
                        profilesResponseData.put("middleName",pr.getMiddleName());
                        profilesResponseData.put("address",pr.getAddress());
                        profilesResponseData.put("convictionType",pr.getConvictionType());
                        profilesResponseData.put("countryCode",pr.getCountryCode());
                        profilesResponseData.put("countryName",pr.getCountryName());
                        profilesResponseData.put("dobOfBirth",pr.getDobOfBirth());
                        profilesResponseData.put("drivingLicenseVerificationResult",pr.getDrivingLicenseVerificationResult());
                        profilesResponseData.put("internalId",pr.getInternalId());
                        profilesResponseData.put("internalIdCriminalRecords",pr.getInternalIdCriminalRecords());
                        profilesResponseData.put("lastName",pr.getLastName());
                        profilesResponseData.put("photoUrl",pr.getPhotoUrl());
                        profilesResponseData.put("postalCode",pr.getPostalCode());
                        profilesResponseData.put("sex",pr.getSex());
                        profilesResponseData.put("source",pr.getSource());
                        profilesResponseData.put("state",pr.getState());
                        profilesResponseData.put("street1",pr.getStreet1());
                        profilesResponseData.put("street2",pr.getStreet2());
                        profilesResponseData.put("verificationResult",pr.getVerificationResult());
                        criminalRecordResponseData.put("profiles",profilesResponseData);
                    }catch(Exception e){}

                    hostDataResponseData.put("criminalRecord",criminalRecordResponseData);
                }catch(Exception e){}

                try {
                    NmResultResponse nrr = hdr.getNmresult();
                    nmResultResponseResponseData.put("createdOnNM",nrr.getCreatedOnNM());
                    nmResultResponseResponseData.put("orderIdNM",nrr.getOrderIdNM());
                    nmResultResponseResponseData.put("resultCountNM",nrr.getResultCountNM());
                    nmResultResponseResponseData.put("orderStatusNM",nrr.getOrderStatusNM());
                    nmResultResponseResponseData.put("orderUrlNM",nrr.getOrderUrlNM());
                    nmResultResponseResponseData.put("productIdNM",nrr.getProductIdNM());
                    nmResultResponseResponseData.put("vital4APIHostTried",nrr.getVital4APIHostTried());
                    hostDataResponseData.put("nmResult",nmResultResponseResponseData);
                }catch(Exception e){}

                try {
                    PepResultResponse prr = hdr.getPepresult();
                    pepResultResponseData.put("createdOnPEP",prr.getCreatedOnPEP());
                    pepResultResponseData.put("orderIdPEP",prr.getOrderIdPEP());
                    pepResultResponseData.put("resultCountPEP",prr.getResultCountPEP());
                    pepResultResponseData.put("productId_PEP",prr.getProductId_PEP());
                    pepResultResponseData.put("orderUrlPEP",prr.getOrderUrlPEP());
                    pepResultResponseData.put("orderStatus_PEP",prr.getOrderStatus_PEP());
                    hostDataResponseData.put("pepresult",pepResultResponseData);
                }catch(Exception e){}

                try {
                    TextMatchResultResponse tmrr = hdr.getTextMatchResult();
                    textMatchResultResponseData.put("address",tmrr.getAddress());
                    textMatchResultResponseData.put("addressCityMatch",tmrr.getAddressCityMatch());
                    textMatchResultResponseData.put("addressLine1Match",tmrr.getAddressLine1Match());
                    textMatchResultResponseData.put("addressLine2Match",tmrr.getAddressLine2Match());
                    textMatchResultResponseData.put("addressZIP4Match",tmrr.getAddressZIP4Match());
                    textMatchResultResponseData.put("addressStateCodeMatch",tmrr.getAddressStateCodeMatch());
                    textMatchResultResponseData.put("addressZIP5Match",tmrr.getAddressZIP5Match());
                    textMatchResultResponseData.put("documentCategoryMatch",tmrr.getDocumentCategoryMatch());
                    textMatchResultResponseData.put("driverLicenseExpirationDateMatch",tmrr.getDriverLicenseExpirationDateMatch());
                    textMatchResultResponseData.put("driverLicenseIssueDateMatch",tmrr.getDriverLicenseIssueDateMatch());
                    textMatchResultResponseData.put("driverLicenseNumberMatch",tmrr.getDriverLicenseNumberMatch());
                    textMatchResultResponseData.put("hostTried",tmrr.getHostTried());
                    textMatchResultResponseData.put("identiFraudHostTried",tmrr.getIdentiFraudHostTried());
                    textMatchResultResponseData.put("personBirthDateMatch",tmrr.getPersonBirthDateMatch());
                    textMatchResultResponseData.put("personFirstNameExactMatch",tmrr.getPersonFirstNameExactMatch());
                    textMatchResultResponseData.put("personFirstNameFuzzyAlternateMatch",tmrr.getPersonFirstNameFuzzyAlternateMatch());
                    textMatchResultResponseData.put("personFirstNameFuzzyPrimaryMatch",tmrr.getPersonFirstNameFuzzyPrimaryMatch());
                    textMatchResultResponseData.put("personLastNameExactMatch",tmrr.getPersonLastNameExactMatch());
                    textMatchResultResponseData.put("personLastNameFuzzyAlternateMatch",tmrr.getPersonLastNameFuzzyAlternateMatch());
                    textMatchResultResponseData.put("personLastNameFuzzyPrimaryMatch",tmrr.getPersonLastNameFuzzyPrimaryMatch());
                    textMatchResultResponseData.put("personMiddleInitialMatch",tmrr.getPersonMiddleInitialMatch());
                    textMatchResultResponseData.put("personMiddleNameExactMatch",tmrr.getPersonMiddleNameExactMatch());
                    textMatchResultResponseData.put("personMiddleNameFuzzyAlternateMatch",tmrr.getPersonMiddleNameFuzzyAlternateMatch());
                    textMatchResultResponseData.put("personMiddleNameFuzzyPrimaryMatch",tmrr.getPersonMiddleNameFuzzyPrimaryMatch());
                    textMatchResultResponseData.put("personSexCodeMatch",tmrr.getPersonSexCodeMatch());
                    textMatchResultResponseData.put("servicePresent",tmrr.getServicePresent());
                    textMatchResultResponseData.put("thirdPartyVerificationResultDescription",tmrr.getThirdPartyVerificationResultDescription());
                    textMatchResultResponseData.put("verificationResult",tmrr.getVerificationResult());
                    hostDataResponseData.put("textMatchResult",textMatchResultResponseData);
                }catch(Exception e){}

                try {
                    SexOffendersResponse sor = hdr.getSexOffenders();

                    try {
                        ProfilesResponse pr2 = sor.getProfiles();
                        profilesResponse2Data.put("city",pr2.getCity());
                        profilesResponse2Data.put("country",pr2.getCountry());
                        profilesResponse2Data.put("fullName",pr2.getFullName());
                        profilesResponse2Data.put("firstName",pr2.getFirstName());
                        profilesResponse2Data.put("middleName",pr2.getMiddleName());
                        profilesResponse2Data.put("address",pr2.getAddress());
                        profilesResponse2Data.put("convictionType",pr2.getConvictionType());
                        profilesResponse2Data.put("countryCode",pr2.getCountryCode());
                        profilesResponse2Data.put("countryName",pr2.getCountryName());
                        profilesResponse2Data.put("dobOfBirth",pr2.getDobOfBirth());
                        profilesResponse2Data.put("drivingLicenseVerificationResult",pr2.getDrivingLicenseVerificationResult());
                        profilesResponse2Data.put("internalId",pr2.getInternalId());
                        profilesResponse2Data.put("internalIdCriminalRecords",pr2.getInternalIdCriminalRecords());
                        profilesResponse2Data.put("lastName",pr2.getLastName());
                        profilesResponse2Data.put("photoUrl",pr2.getPhotoUrl());
                        profilesResponse2Data.put("postalCode",pr2.getPostalCode());
                        profilesResponse2Data.put("sex",pr2.getSex());
                        profilesResponse2Data.put("source",pr2.getSource());
                        profilesResponse2Data.put("state",pr2.getState());
                        profilesResponse2Data.put("street1",pr2.getStreet1());
                        profilesResponse2Data.put("street2",pr2.getStreet2());
                        profilesResponse2Data.put("verificationResult",pr2.getVerificationResult());
                        sexOffendersResponseData.put("profiles",profilesResponse2Data);
                    }catch(Exception e){}

                    hostDataResponseData.put("sexOffenders",sexOffendersResponseData);
                }catch(Exception e){}

                try {
                    WlsResultResponse wrr = hdr.getWlsresult();
                    wlsResultResponseData.put("createdOnWLS",wrr.getCreatedOnWLS());
                    wlsResultResponseData.put("orderIdWLS",wrr.getOrderIdWLS());
                    wlsResultResponseData.put("resultCountWLS",wrr.getResultCountWLS());
                    wlsResultResponseData.put("productIdWLS",wrr.getProductIdWLS());
                    wlsResultResponseData.put("orderStatusWLS",wrr.getOrderStatusWLS());
                    wlsResultResponseData.put("orderUrlWLS",wrr.getOrderUrlWLS());
                    hostDataResponseData.put("wlsresult",wlsResultResponseData);
                }catch(Exception e){}
                responseCustomerVerifyData.put("hostDataResponse",hostDataResponseData);
            }catch(Exception e){}

            jo.put("responseCustomerVerifyData", responseCustomerVerifyData);
        }catch(Exception e){}

        try {
            JSONObject additionalCustomerLiveCheckResponseData = new JSONObject();

            AdditionalCustomerLiveCheckResponseData aclcrd = response.getResult().getAdditionalData();
            additionalCustomerLiveCheckResponseData.put("liveFaceDetectionFlag",aclcrd.getLiveFaceDetectionFlag());

            jo.put("additionalCustomerLiveCheckResponseData", additionalCustomerLiveCheckResponseData);
        }catch(Exception e){}

        return jo.toString();
    }
}
