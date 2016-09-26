# AES-Encryption
AES Encryption allow developer to encrypt or decrypt text or response of server on client side of android. By reading javascrpty is another advantage to use on server side and also in iOS side as well. Using CryptoJS.swift Cross device cryptography in swift using the Crypto JS library (Thanks to Jeff Mott). Allows you to share the same crypto between a native iOS/OSX application and a web application.

In this android example we have use aes javascript function to encryption & decryption. Using JSContext object we can evalute javascript file and JSValue object we can call javascript function. As below code snipet. 

Example:
	
	JSContext jsContext = new JSContext();
	jsContext.evaluateScript(readAssetFile("aes.js"));
	JSValue encryptFunction = jsContext.property(property);
	encryptFunction.toFunction().call(null,text,password).toString();

For this we have to put dependency in app module. 

	compile 'com.github.ericwlange:AndroidJSCore:3.0.1'

and in project level module 

	allprojects {
	    repositories {
		jcenter()
		mavenLocal()
		maven { url "https://jitpack.io" }
	    }
	}
After do all this apk size will increase to 40 mb. There are 7 ABIs, and the library compresses to about 6MB per ABI. That's where the bloated size is coming from. I will try to figure out if there is a way to reduce this footprint, but in the meantime. These are ABI in android device 'armeabi', 'armeabi-v7a', 'arm64-v8a', 'mips', 'mips64','x86', "x86_64".

We can put split APK code into app module leve as below:


	splits {
	    abi {
		enable true
		reset()
		include 'armeabi', 'armeabi-v7a', 'arm64-v8a', 'mips', 'mips64','x86', "x86_64"
		universalApk false
	    }
		project.ext.versionCodes = ['armeabi': 1, 'armeabi-v7a': 2, 'arm64-v8a': 3, 'mips': 	5, 'mips64': 6, 'x86': 8, 'x86_64': 9]

		android.applicationVariants.all { variant ->
			variant.outputs.each { output ->
			output.versionCodeOverride =
		    project.ext.versionCodes.get(output.getFilter(
				com.android.build.OutputFile.ABI), 0) * 10000000 + 					android.defaultConfig.versionCode
		    }
		}
	}
Now Gradle will generate one APK per CPU reducing the size even further! 
You can check more details from here https://realm.io/news/reducing-apk-size-native-libraries/
And how to deply your apk on google play store then you can find information from here https://androidbycode.wordpress.com/2015/07/07/android-ndk-a-guide-to-deploying-apps-with-native-libraries/

