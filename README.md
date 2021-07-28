# PrettyPersianNumbers

<!-- ALL-CONTRIBUTORS-BADGE:START - Do not remove or modify this section -->
[![All Contributors](https://img.shields.io/badge/all_contributors-2-orange.svg?style=flat-square)](#contributors-)
<!-- ALL-CONTRIBUTORS-BADGE:END -->

<img src="logo.png" title="" alt="logo" data-align="center">

  [![Build](https://api.travis-ci.com/yamin8000/PrettyPersianNumbers.svg?branch=master)](https://travis-ci.com/github/yamin8000/PrettyPersianNumbers)

> Convert numbers to Persian words  

---  

| -     | -                               |
| -----:|:------------------------------- |
| 📺    | [Preview](#Preview)             |
| 📱    | [Compatibility](#Compatibility) |
| 💻    | [Usage](#Usage)                 |
| 📩    | [Download](#Download)           |
| 📋    | [Features](#Features)           |
| 🧾    | [Changelog](#Changelog)         |
| 🏆    | [Credits](#Credits)             |
| 👨‍💻 | [Contribution](#Contributors ✨) |
| ⚖️    | [License](#License)             |

---

## Preview

![ScreenShot](/screen.gif)

[Check preview on imgur](https://imgur.com/unZlSke)

## Compatibility

  Android Jellybean **4.1+**/SDK 16+  

## Usage

**Kotlin**
You just need to create an instance from `Digits` class and call `spellToFarsi` method. input can be any number in different data types like `Byte`, `Short`, `Int`, `Long`, `Float`, `Double`, `BigInteger`, `BigDecimal` or numbers as a `String`.

```kotlin
    val word1 : String = Digits().spellToFarsi(input)
    val word2 : String = Digits().spellToFarsi(12) // دوازده
    //دوازده میلیارد و یک صد و بیست و سه میلیون و سی صد و دوازده هزار و یک صد و بیست و سه
    val word3 : String = Digits().spellToFarsi(12_123_312_123)
    //پنج میلیون و یک صد و بیست و یک هزار و سی صد و بیست و یک
    val word4 : String = Digits().spellToFarsi("5121321")
    //چهارده هزار و پانصد ریال
    val money1 : String = Digits().spellToIranMoney("14500")
    //چهارده هزار و پانصد تومان
    val money2 : String = Digits().spellToIranMoney("14500", IranCurrency.TOMAN)
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
    implementation 'com.github.yamin8000:PrettyPersianNumbers:1.0.3'
}
```

### Maven

 Add the dependency  

```xml
<dependency>
  <groupId>com.github.yamin8000</groupId>
  <artifactId>PrettyPersianNumbers</artifactId>
  <version>1.0.3</version>
  <type>aar</type>
</dependency>  
```

### Gradle Kotlin DSL

```groovy
dependencies {
    implementation("com.github.yamin8000:PrettyPersianNumbers:1.0.3")
}
```

## Features

- Any number that could be fitted in `BigInteger` can be converted to Persian words using this library From Zero to Vigintillion or more
- Convert numbers from `Byte`, `Short`, `Int`, `Long`, `Float`, `Double`, `BigInteger`, `BigDecimal`
- Convert numbers straight to currency like: چهارده هزار و پانصد تومان see [Usage](https://github.com/yamin8000/PrettyPersianNumbers#Usage)
- Large numbers are named using [short-scale standard](https://en.wikipedia.org/wiki/Long_and_short_scales)
- Negative numbers are supported

## Changelog

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

## Contributors ✨

Thanks goes to these wonderful people ([emoji key](https://allcontributors.org/docs/en/emoji-key)):

<!-- ALL-CONTRIBUTORS-LIST:START - Do not remove or modify this section -->
<!-- prettier-ignore-start -->
<!-- markdownlint-disable -->
<table>
  <tr>
    <td align="center"><a href="http://yyss.ir"><img src="https://avatars.githubusercontent.com/u/9123711?v=4?s=100" width="100px;" alt=""/><br /><sub><b>Yaghoob Siahmargooei</b></sub></a><br /><a href="https://github.com/yamin8000/PrettyPersianNumbers/issues?q=author%3Ayaghoob" title="Bug reports">🐛</a> <a href="https://github.com/yamin8000/PrettyPersianNumbers/commits?author=yaghoob" title="Code">💻</a></td>
    <td align="center"><a href="https://github.com/yamin8000"><img src="https://avatars.githubusercontent.com/u/5001708?v=4?s=100" width="100px;" alt=""/><br /><sub><b>YaMiN</b></sub></a><br /><a href="https://github.com/yamin8000/PrettyPersianNumbers/commits?author=yamin8000" title="Code">💻</a></td>
  </tr>
</table>

<!-- markdownlint-restore -->
<!-- prettier-ignore-end -->

<!-- ALL-CONTRIBUTORS-LIST:END -->

This project follows the [all-contributors](https://github.com/all-contributors/all-contributors) specification. Contributions of any kind welcome!