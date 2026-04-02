import React from 'react'
import { View, TouchableOpacity, Text, Image, Platform } from 'react-native'
import { Container, ScrollView, Picker, NativeBaseProvider, Center, VStack, Pressable   } from "native-base";
import * as constant from '../Constant'
import styles from '../Styles'
import { NativeModules, NativeEventEmitter } from 'react-native';
import { TextInput } from "react-native";
import { LogBox } from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';

LogBox.ignoreLogs(['new NativeEventEmitter']);
LogBox.ignoreLogs(['Warning: ...']);
LogBox.ignoreAllLogs(); 
const { IDMissionSDK } = NativeModules;

export default class Home extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            selected: '',
            images: '',
            uniqueCustomerNumber: '',
            apiBaseUrl: '',
            authUrl: '',
            debug: '',
            accessToken: '',
            event: [
                "Select Feature"
            ]
        }
    }

    componentDidMount() {
        const eventEmitter = new NativeEventEmitter(IDMissionSDK);

        eventEmitter.addListener('onSessionConnect', (event) => { });

        eventEmitter.addListener('DataCallback', (event) => {
            console.log("Responsde")
            var data = JSON.stringify(event);
            this.props.navigation.navigate("ResultScreen", { eventResponse: event, eventName: "Data" })
        });

    }

    onInit = () => {
        IDMissionSDK.initializeSDK(
            this.state.apiBaseUrl,
            this.state.authUrl,
            this.state.debug,
            this.state.accessToken
        );
    }

    onServiceID20 = () => {
        IDMissionSDK.serviceID20();
    }

    onServiceID10 = () => {
        IDMissionSDK.serviceID10();
    }

    onServiceID50 = () => {
	   IDMissionSDK.serviceID50(this.state.uniqueCustomerNumber);	
    }

    onServiceID175 = () => {
        IDMissionSDK.serviceID175(this.state.uniqueCustomerNumber);
    }

    onServiceID105 = () => {
        IDMissionSDK.serviceID105(this.state.uniqueCustomerNumber);
    }

    onServiceID185 = () => {
        IDMissionSDK.serviceID185();
    }
         
    onServiceID660 = () => {
        IDMissionSDK.serviceID660();
    }
	
	onSubmit = () => {
        IDMissionSDK.submitResult();
    }

    saveUniqueCustomerNumber = (text) => {
        this.setState({
          uniqueCustomerNumber: text
        })        
    }

    saveApiBaseUrl = (text) => {
        this.setState({
          apiBaseUrl: text
        })        
    }

    saveAuthUrl = (text) => {
        this.setState({
          authUrl: text
        })        
    }

    saveDebug = (text) => {
        this.setState({
          debug: text
        })        
    }       

    saveAccessToken = (text) => {
        this.setState({
          accessToken: text
        })        
    }           

  

    render() {
        return (
            <NativeBaseProvider>
            <SafeAreaView>
             <Container>
                
                <ScrollView contentContainerStyle={{ justifyContent: 'center', margin: 20 }}>
                   
                   <TextInput style={{borderBottomWidth: 1,fontSize: constant.smallFont}} placeholder="Api Base Url" onChangeText={(text) => { this.saveApiBaseUrl(text) }}/>

                   <TextInput style={{borderBottomWidth: 1,fontSize: constant.smallFont}} placeholder="Auth Url" onChangeText={(text) => { this.saveAuthUrl(text) }}/>

                   <TextInput style={{borderBottomWidth: 1,fontSize: constant.smallFont}} placeholder="Debug" onChangeText={(text) => { this.saveDebug(text) }}/>

                   <TextInput style={{borderBottomWidth: 1,fontSize: constant.smallFont}} placeholder="Access Token" onChangeText={(text) => { this.saveAccessToken(text) }}/>

                   <TextInput style={{borderBottomWidth: 1,fontSize: constant.smallFont}} placeholder="Unique Customer Number" onChangeText={() => { this.saveUniqueCustomerNumber() }}/>

                    <Pressable style={{backgroundColor: '#00a5a4', borderWidth: 2, marginTop: 10}} onPress={() => { this.onInit() }}>
                        <Text style={{fontSize: constant.smallFont, padding: 10}}>Initialize</Text>
                    </Pressable>

                    <Pressable style={{backgroundColor: '#00a5a4', borderWidth: 2, marginTop: 10}} onPress={() => { this.onServiceID20() }}>
                        <Text style={{fontSize: constant.smallFont, padding: 10}}>ID Validation</Text>
                    </Pressable>

                    <Pressable style={{backgroundColor: '#00a5a4', borderWidth: 2, marginTop: 10}} onPress={() => { this.onServiceID10() }}>
                        <Text style={{fontSize: constant.smallFont, padding: 10}}>ID Validation and Match Face</Text>
                    </Pressable>

                    <Pressable style={{backgroundColor: '#00a5a4', borderWidth: 2, marginTop: 10}} onPress={() => { this.onServiceID50() }}>
                        <Text style={{fontSize: constant.smallFont, padding: 10}}>ID Validation and Customer Enroll</Text>
                    </Pressable>

                    <Pressable style={{backgroundColor: '#00a5a4', borderWidth: 2, marginTop: 10}} onPress={() => { this.onServiceID175() }}>
                        <Text style={{fontSize: constant.smallFont, padding: 10}}>Customer Enroll Biometrics</Text>
                    </Pressable>

                    <Pressable style={{backgroundColor: '#00a5a4', borderWidth: 2, marginTop: 10}} onPress={() => { this.onServiceID105() }}>
                        <Text style={{fontSize: constant.smallFont, padding: 10}}>Customer Verification</Text>
                    </Pressable>

                    <Pressable style={{backgroundColor: '#00a5a4', borderWidth: 2, marginTop: 10}} onPress={() => { this.onServiceID185() }}>
                        <Text style={{fontSize: constant.smallFont, padding: 10}}>Identify Customer</Text>
                    </Pressable>                    

                    <Pressable style={{backgroundColor: '#00a5a4', borderWidth: 2, marginTop: 10}} onPress={() => { this.onServiceID660() }}>
                        <Text style={{fontSize: constant.smallFont, padding: 10}}>Live Face Check</Text>
                    </Pressable>                

                    <Pressable style={{backgroundColor: '#00a5a4', borderWidth: 2, marginTop: 10}} onPress={() => { this.onSubmit() }}>
                        <Text style={{fontSize: constant.smallFont, padding: 10}}>Submit</Text>
                    </Pressable>  
                </ScrollView>
                
            </Container>
            </SafeAreaView>
            </NativeBaseProvider>
        )
    }
}
