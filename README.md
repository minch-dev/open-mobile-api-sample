# open-mobile-api-sample 2.0
Sample app that uses Google implementation of `OMAPI` that can be built in Android Studio Hedgehog.

# Android 9 and later
Built with `minSdk=27` just in case _someone_ manages to build `SecureElement` and `OMAPI` for Android 8.1.
This results in a lot of red underlines across the code, so if it bothers you set minimum to `28` in project settings.

# SIMalliance vs Google
Some people (including but not limited to: me) are getting confused with the og `OMAPI` library that was around since like Android 6: `org.simalliance.openmobileapi`.
This is not the version you probably want or need these days. The modern implementation by Google shipped by default with Android 9+ (SDK 28+) is called `android.se.omapi` and it is accompanied by `com.android.se` application called `SecureElement`.
So just installing the old implementation on Android 8.1 and earlier is not going to help you get `OMAPI` on your device, those API's are slightly different and have different package names.

# Purpose
This application is as simple and small as possible, not cluttered with excessive ui and absolutely fits the purpose of debugging `OMAPI` support on virtually any device. The debug build signed with debug keys is provided via releases section.