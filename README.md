### saro commons
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/me.saro/commons/badge.svg)](https://maven-badges.herokuapp.com/maven-central/me.saro/commons)
[![GitHub license](https://img.shields.io/github/license/saro-lab/commons.svg)](https://github.com/saro-lab/commons/blob/master/LICENSE)


# QUICK START

## maven

``` xml
<dependency>
  <groupId>me.saro</groupId>
  <artifactId>commons</artifactId>
  <version>0.0.10</version>
</dependency>
```

## gradle

```
compile 'me.saro:commons:0.0.10'
```

## repository
- https://search.maven.org/artifact/me.saro/commons
- http://central.maven.org/maven2/me/saro/commons/
- https://mvnrepository.com/artifact/me.saro/commons

## see
- [가리사니 개발자공간](https://gs.saro.me)

# STATIC

## Converter
#### me.saro.commons.Converter

```
Converter.asList(T...)
Converter.splitCsvLine(String)
Converter.toByteArrayOutputStream(InputStream, int)
Converter.toBytes(InputStream, int)
Converter.toClassByJson(String, TypeReference<T>)
Converter.toHash(String, byte[])
Converter.toHash(String, String)
Converter.toHash(String, String, String)
Converter.toHex(byte[])
Converter.toJson(Object)
Converter.toList(Enumeration<T>)
Converter.toList(Iterable<T>)
Converter.toMap(Object...)
Converter.toMapByJsonObject(String)
Converter.toMapListByJsonArray(String)
Converter.toStream(Enumeration<T>)
Converter.toStream(Enumeration<T>, boolean)
Converter.toStream(Iterable<T>)
Converter.toStream(Iterable<T>, boolean)
Converter.toStreamByResultSet(ResultSet, ThrowableBiFunction<String[], Object[], R>)
Converter.toStreamByResultSet(ResultSet, ThrowableFunction<ResultSet, R>)
Converter.toStreamLineNotCloseByTextInputStream(InputStream, String)
Converter.toString(Exception)
Converter.toString(File, String)
Converter.toString(InputStream, String)
Converter.toStringNotClose(InputStream, String)
```

## Utils
#### me.saro.commons.Utils
```
Utils.evl(String...)
Utils.nvl(T...)
Utils.inputStreamReader(InputStream, StreamReadConsumer)
Utils.openZipFromFile(File, ThrowableTriConsumer<String, ZipEntry, InputStream>)
Utils.openZipFromWeb(Web, ThrowableTriConsumer<String, ZipEntry, InputStream>)
Utils.openZipStreamNotClose(InputStream, ThrowableTriConsumer<String, ZipEntry, InputStream>)
Utils.random(long, long)
```

## Files
#### me.saro.commons.Files
```
Files.createFile(File, boolean, InputStream)
Files.createParentDirectoryForFile(File)
```

## Valids
#### me.saro.commons.Valids
```
Valids.isMail(String, int)
```

## Lambdas
#### me.saro.commons.Lambdas
```
Lambdas.runtime(ThrowableRunnable)
Lambdas.runtime(ThrowableSupplier<R>)
Lambdas.runtime(ThrowableConsumer<T>)
Lambdas.runtime(ThrowableBiConsumer<T, U>)
Lambdas.runtime(ThrowableFunction<T, R>)
Lambdas.runtime(ThrowableBiFunction<T, U, R>)
```

# INSTANCE

## Web
#### me.saro.commons.web.Web

```
Web.custom(String, String)
Web.delete(String)
Web.get(String)
Web.patch(String)
Web.post(String)
Web.put(String)
addUrlParameter(String, String)
readRawResultStream(ThrowableConsumer<InputStream>)
saveFile(File, boolean)
setContentType(String)
setHeader(String, String)
setIgnoreCertificate(boolean)
setRequestCharset(String)
setResponseCharset(String)
toCustom(ThrowableFunction<InputStream, R>)
toCustom(WebResult<R>, ThrowableFunction<InputStream, R>)
toJsonReader()
toJsonTypeReference(TypeReference<T>)
toMapByJsonObject()
toMapListByJsonArray()
toPlainText()
writeBody(byte[])
writeBody(String)
writeBodyParameter(String, String)
writeJsonByClass(Object)
```

## Ftp
#### me.saro.commons.Ftp
```
Ftp.open(InetAddress, int, String, String)
getWorkingDirectory()
listDirectories()
listDirectories(Predicate<FTPFile>)
listFiles()
listFiles(Predicate<FTPFile>)
read(String, File)
send(String, File)
setFileTypeAscii()
setFileTypeBinary()
setWorkingDirectory(String)
close()
```

## DateFormat
#### me.saro.commons.DateFormat
```
DateFormat.format(String, String, String)
DateFormat.now()
DateFormat.parse(Date)
DateFormat.parse(long)
DateFormat.parse(String, String)
DateFormat.valid(String, String)
DateFormat.format(String)
addDates(int)
addHours(int)
addMilliseconds(int)
addMinutes(int)
addMonth(int)
addYear(int)
getDate()
getDayOfWeek()
getHours()
getMilliseconds()
getMinute()
getMonth()
getSeconds()
getTimeInMillis()
getWeekOfMonth()
getWeekOfYear()
getYear()
setDate(int)
setHours(int)
setMilliseconds(int)
setMinutes(int)
setMonth(int)
setSeconds(int)
setTimeInMillis(long)
setYear(int)
toDate()
toISO8601()
toString()
toString(String)
clone()
equals(Object)
```

## JsonReader
#### me.saro.commons.JsonReader
```
JsonReader.create(String)
JsonReader.emptyList()
JsonReader.emptyObject()
get(int)
get(String)
getInt(String, int)
getString(String)
into(String)
isArray()
isObject()
length()
toList()
toString()
clone()
equals(Object)
```


# INTERFACE
#### me.saro.commons.function.*
```
ThrowableRunnable
ThrowableSupplier<R>
ThrowablePredicate<T>
ThrowableConsumer<T>
ThrowableBiConsumer<T, U>
ThrowableTriConsumer<T, U, V>
ThrowableFunction<T, R>
ThrowableBiFunction<T, U, R>
ThrowableTriFunction<T, U, V, R>
```
