# **Android TextHighlighter**
[![version 1.0.2](https://img.shields.io/badge/version-1.0.2-green.svg)]()
[![API Level ≥9](https://img.shields.io/badge/platform-android-lightgrey.svg)](https://developer.android.com/index.html)
[![API ≥2.3](https://img.shields.io/badge/android-API%20Level%20%E2%89%A59-blue.svg)](https://developer.android.com/about/versions/android-2.3.html)
[![API ≥9](https://img.shields.io/badge/android-API%20%E2%89%A52.3-blue.svg)](https://developer.android.com/about/versions/android-2.3.html)
[![MIT LICENSE](https://img.shields.io/github/license/mashape/apistatus.svg)](https://spdx.org/licenses/MIT.html#licenseText)
[![javadoc](https://img.shields.io/badge/document-javadoc-yellow.svg)](https://xeoh.github.io/TextHighlighter/)

## Introduction
highlights every View which inherits TextView(i.e. TextView, Button, EditText). Set targets and Colors. Then highlight any word.

<img
  src="./images/TextHighlighter.gif"
  dynsrc="./images/TextHighlighter.gif"
  loop=infinite
  alt="Sample">

## Requirements
Android API ≥ 2.3 (API Level 9)

## Gradle
You can import TextHighlighter from **jcenter**.
```gradle
repositories {
    jcenter()
}

dependencies {
    compile 'com.xeoh.android:text-highlighter:1.0.2'
}
```

## Usage
```java
// Initialize TextHighlighter
private TextHighlighter textHighlighter = new TextHighlighter()
      .setBackgroundColor(Color.parse("#FFFF00"))
      .setForegroundColor(Color.RED)
      .setBold(true)
      .setItalic(true)
      .addTarget(findViewById(R.id.anyTextView))
      .highlight("word", TextHighlighter.BASE_MATCHER);

// invalidate after add more targets
textHighlighter.addTarget(findViewById(R.id.anyButton))
      .invalidate(TextHighlighter.BASE_MATCHER);

// invalidate after color changes
textHighlighter.setForegroundColor(Color.GREEN)
      .invalidate(TextHighlighter.BASE_MATCHER);

// change matcher to case insensitive
textHighlighter.invalidate(TextHighlighter.CASE_INSENSITIVE_MATCHER);

/*
You can use multiple TextHighlighter for multiple keyword or multiple
styles. However, if two highlighter highlights same word, priority depends
on order of highlight(...) function call
*/
```

See [sample application](https://github.com/xeoh/TextHighlighter/tree/master/sample)

## Version History

- **1.0.0** Testing version. (Not published)
- **1.0.1** First Deploy.
- **1.0.2** Add bold and italic feature.

## License
TextHighlighter is available under the MIT license. See the LICENSE file for more info.