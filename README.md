# Currency
## App Description
+ Currency is a currency exchange rates converter.
+ Currency supports almost currencies
+ It based on https://openexchangerates.org/

## How to use the app
Open the app with the Internet -> The app will display all currencies and base currency above
+ Type a number to the box -> The app will exchange and display all currencies
+ Type a keyword on the search box -> The app will filter the currencies list by keyword
It supports offline mode

## How to build the app
+ Should open project by Android Studio<br/>
+ It can config `OPEN_EXCHANGE_RATES_SERVICE_APP_ID_PROD` if we have a specific key for Production build

## Challenge when building the app
I faced 3 problems when I build the app:
1. App Architecture: I would like to make an app with architecture to easy maintenance and scalable and testable. So I decided on a modularization app and applied Clean Architecture, MVVM. More details below
2. Dagger: There are many modules in the App Architecture, so it's hard to connect with each other. I created a BaseComponent in the Base module and every feature module need to be dependent to BaseComponent
3. Paging list with online and offline mode: I used the Paging library in Android Jetpack and created a repository that responses to decide online (data remote by Retrofit) or offline (data local by Room)

I split the app into 2 modules:<br/>
1. Base Module: BaseActivity, BaseFragment, BaseComponent,...<br/>
2. Design System Module: Colors, Styles, Spacing, Icon Size, Font Size,...<br/>

* I used the modules to show, how I work with multi module application
  
## Advantage of Modularization:
+ Smaller components are easier to maintain.<br/>
+ Decrease build times<br/>
+ Components with high cohesion can be re-used again.<br/>
+ The team can decide architecture on own features<br/>
+ Some libraries in the project: ViewBinding, Dagger, Retrofit. Jetpack components: ViewModel, Navigation, Paging, Room

## Test
+ I don't have enough time to write all tests, so I try my best.
