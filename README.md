# PrettyPersianNumbers

<img src="logo.png" title="" alt="logo" data-align="center">

## Preview

![ScreenShot](/screen.gif)

[Check preview on imgur](https://imgur.com/unZlSke)

## Compatibility

> [!important]
> Android Jellybean **4.1+**/SDK 16+

## Usage

**Kotlin**

```kotlin
val word1: String = PersianDigits.spellToPersian(input)
val word2: String = PersianDigits.spellToPersian(12) // دوازده
//دوازده میلیارد و یک صد و بیست و سه میلیون و سی صد و دوازده هزار و یک صد و بیست و سه
val word3: String = PersianDigits.spellToPersian(12_123_312_123)
//پنج میلیون و یک صد و بیست و یک هزار و سی صد و بیست و یک
val word4: String = PersianDigits.spellToPersian("5121321")
//سه ممیز چهارده، صدم
val decimalWord: String = PersianDigits.spellToPersian(3.14)
```
> [!tip]
> In Kotlin, you can also take advantage of extension methods like:

```kotlin
500.spellToPersian()
"12.51".spellToPersian()
```

**Java**

```java
String persianNumber=PersianHelpersJava.spellToFarsi("1231");
```

## Download

> [!important]
> Find the latest version [here](https://central.sonatype.com/artifact/com.github.yamin8000/PrettyPersianNumbers).

### Gradle Groovy DSL

**Step 1.** Add the Maven repository to your build file  
Add it to your root build.gradle at the end of repositories:

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

```XML

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

- Any number that could be fitted in `BigInteger` can be converted to Persian words using this
  library From Zero
  to `Vigintillion` or more
- Convert numbers from `Byte`, `Short`, `Int`, `Long`, `Float`, `Double`, `BigInteger`, `BigDecimal`
- Large numbers are named
  using [short-scale standard](https://en.wikipedia.org/wiki/Long_and_short_scales)
- Negative numbers are supported

## Credits

[https://github.com/yaghoob](https://github.com/yaghoob)

## License

> [!important]
> Pretty Persian Numbers is licensed under the **[GNU General  
> Public License v3.0](./LICENSE)**  
> Permissions of this strong copyleft license are conditioned on making  
> available complete source code of licensed works and modifications,  
> which include larger works using a licensed work, under the same  
> license. Copyright and license notices must be preserved. Contributors  
> provide an express grant of patent rights.
