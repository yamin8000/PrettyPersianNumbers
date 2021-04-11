
# Pretty Persian Numbers  
  
> Convert numbers to Persian words  
  
|-|-|  
|--|--|  
|📺|[Preview](#Preview)|  
|📱|[Compatibility](#Compatibility)|  
|💻|[Usage](#Usage)|  
|📩|[Download](#Download)|  
|📋|[Features](#Features)|  
|🧾|[Changelog](#Changelog)|  
|🏆|[Credits](#Credits)|  
|👨‍💻|[Contribution](#Contribution)|  
|⚖️|[License](#License)|  
  
  
## Preview  
  
## Compatibility  
  Android Jellybean 4.1+/SDK 16+  
## Usage  
**Kotlin**
You just need to create an instance from `Digits` class and call `spellToFarsi` method. input can be any number in different data types like `Byte`, `Short`, `Int`, `Long` or numbers as a `String`.
```kotlin
    val word1 : String = Digits.spellToFarsi(input)
    val word2 : String = Digits.spellToFarsi(12) // دوازده
    //دوازده میلیارد و یک صد و بیست و سه میلیون و سی صد و دوازده هزار و یک صد و بیست و سه
    val word3 : String = Digits.spellToFarsi(12_123_312_123)
    //پنج میلیون و یک صد و بیست و یک هزار و سی صد و بیست و یک
    val word4 : String = Digits.spellToFarsi("5121321")
```
**Java**
```java
String persianNumber = new Digits().spellToFarsi("1231");
```
## Download  
Find latest version from [Maven Artifact](https://search.maven.org/artifact/com.github.yamin8000/PrettyPersianNumbers)
### Gradle Groovy DSL 
**Step 1.** Add the Maven repository to your build file  
Add it in your root build.gradle at the end of repositories:  
```groovy
    repositories {  
        mavenCentral()  
    }
```
**Step 2.** Add the dependency  
```groovy  
dependencies {  
    implementation 'com.github.yamin8000:PrettyPersianNumbers:1.0.1'  
}  
```  
### Maven  
 Add the dependency  
```xml
<dependency>
  <groupId>com.github.yamin8000</groupId>
  <artifactId>PrettyPersianNumbers</artifactId>
  <version>1.0.1</version>
  <type>aar</type>
</dependency>  
```  
### Gradle Kotlin DSL
```groovy  
dependencies {  
implementation("com.github.yamin8000:PrettyPersianNumbers:1.0.1")
}  
``` 
## Features  
 - Any number that could be fitted in `BigInteger` can be converted to Persian words using this library
 - Large numbers are named using [short-scale standard](https://en.wikipedia.org/wiki/Long_and_short_scales)
 - Negative numbers are supported
 - TODO adding support for `Float`/`Double` numbers

## Changelog  
  **1.0.1** bug fixes, added support for big numbers
  **1.0.0** initial version
## Credits  
  [https://github.com/yaghoob](https://github.com/yaghoob)
## Contribution  
Any contribution is welcome, For Persian speakers (:iran: :afghanistan:) creating issues in Persian is also allowed.  
## License  
> Pretty Persian Numbers is licensed under the **[GNU General  
> Public License v3.0](./LICENSE)**  
> Permissions of this strong copyleft license are conditioned on making  
> available complete source code of licensed works and modifications,  
> which include larger works using a licensed work, under the same  
> license. Copyright and license notices must be preserved. Contributors  
> provide an express grant of patent rights.
