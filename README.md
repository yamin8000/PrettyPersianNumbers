# PrettyPersianNumbers

<img src="logo.png" title="" alt="logo" data-align="center">

[![javadoc](https://javadoc.io/badge2/com.github.yamin8000/PrettyPersianNumbers/javadoc.svg)](https://javadoc.io/doc/com.github.yamin8000/PrettyPersianNumbers)
> Convert numbers to Persian words

---  

|  -   |                -                 |
|:----:|:--------------------------------:|
|  📺  |       [Preview](#Preview)        |
|  📱  | [Compatibility](#Compatibility)  |
|  💻  |         [Usage](#Usage)          |
|  📩  |      [Download](#Download)       |
|  📋  |      [Features](#Features)       |
|  🧾  |     [Changelog](#Changelog)      |
|  🏆  |       [Credits](#Credits)        |
|  ⚖️  |       [License](#License)        |

---

## Preview

![ScreenShot](/screen.gif)

[Check preview on imgur](https://imgur.com/unZlSke)

## Compatibility

Android Jellybean **4.1+**/SDK 16+

## Usage

**Kotlin**

You just need to create an instance from `Digits` class and call `spellToFarsi` method. input can be any number in
different data types like `Byte`, `Short`, `Int`, `Long`, `Float`, `Double`, `BigInteger`
, `BigDecimal` or numbers as a `String`.

```kotlin
val word1: String = Digits().spellToFarsi(input)
val word2: String = Digits().spellToFarsi(12) // دوازده
//دوازده میلیارد و یک صد و بیست و سه میلیون و سی صد و دوازده هزار و یک صد و بیست و سه
val word3: String = Digits().spellToFarsi(12_123_312_123)
//پنج میلیون و یک صد و بیست و یک هزار و سی صد و بیست و یک
val word4: String = Digits().spellToFarsi("5121321")
//سه ممیز چهارده، صدم
val decimalWord: String = Digits().spelltoFarsi(3.14)
```

in Kotlin, you can also take advantage of extension methods like so:

```kotlin
500.spell()
"12.51".spell()
```

**Java**

```java
String persianNumber=new Digits().spellToFarsi("1231");
```

## Download

Find the latest version
from [https://central.sonatype.com/artifact/com.github.yamin8000/PrettyPersianNumbers/1.1.1/versions)

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
    implementation 'com.github.yamin8000:PrettyPersianNumbers:$digits_last_version'
}
```

### Maven

Add the dependency

```xml

<dependency>
    <groupId>com.github.yamin8000</groupId>
    <artifactId>PrettyPersianNumbers</artifactId>
    <version>$digits_last_version</version>
    <type>aar</type>
</dependency>  
```

### Gradle Kotlin DSL

```groovy
dependencies {
    implementation("com.github.yamin8000:PrettyPersianNumbers:$digits_last_version")
}
```

## Features

- Any number that could be fitted in `BigInteger` can be converted to Persian words using this library From Zero
  to `Vigintillion` or more
- Convert numbers from `Byte`, `Short`, `Int`, `Long`, `Float`, `Double`, `BigInteger`, `BigDecimal`
- Large numbers are named using [short-scale standard](https://en.wikipedia.org/wiki/Long_and_short_scales)
- Negative numbers are supported

## Changelog

- **1.1.0** [more info](https://github.com/yamin8000/PrettyPersianNumbers/releases/tag/1.1.0)
- **1.0.5** [more info](https://github.com/yamin8000/PrettyPersianNumbers/releases/tag/1.0.5)
- **1.0.3** add support for decimal numbers
- **1.0.2** bug fixes
- **1.0.1** bug fixes, added support for big numbers
- **1.0.0** initial version

## Credits

[https://github.com/yaghoob](https://github.com/yaghoob)

## License

> Pretty Persian Numbers is licensed under the **[GNU General  
> Public License v3.0](./LICENSE)**  
> Permissions of this strong copyleft license are conditioned on making  
> available complete source code of licensed works and modifications,  
> which include larger works using a licensed work, under the same  
> license. Copyright and license notices must be preserved. Contributors  
> provide an express grant of patent rights.
