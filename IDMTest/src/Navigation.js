import * as React from 'react';
import { NavigationContainer } from '@react-navigation/native';
import { createNativeStackNavigator } from '@react-navigation/native-stack';
import ResultScreen from './Home/ResultScreen'
import Home from './Home/Home'

const Stack = createNativeStackNavigator();

function App() {
  return (
     <NavigationContainer >
      <Stack.Navigator screenOptions={{ initialRouteName: "Home", headerShown: false }}>
     
        <Stack.Screen name="Home" component={Home} />
        <Stack.Screen name="ResultScreen" component={ResultScreen} />
      
      </Stack.Navigator>
    </NavigationContainer>
  );
}

export default App;