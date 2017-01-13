# Android-SimpleFirebaseChat

Simple chat application that contains a chat room and authenticated users list with their status info such as Online/Offline.

## Integrated with some Firebase features
* Authentication
* Realtime Database
* Analytics
* Notifications

### How to add Firebase to your app?

Create or Import Google Project on Firebase Console.       
Enter your app's package name.    
At the end , download google-services.json file and copy this into project's app/ module folder.

To add SDK , 
- Add rules to your root-level build.gradle    
classpath 'com.google.gms:google-services:3.0.0'
- Add the apply plugin line at the bottom of the file to enable the Gradle plugin in your module Gradle file (usually the app/build.gradle),
apply plugin: 'com.google.gms.google-services'
