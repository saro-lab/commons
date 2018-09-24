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

``` js
compile 'me.saro:commons:0.0.10'
```

## repository
- https://search.maven.org/artifact/me.saro/commons
- http://central.maven.org/maven2/me/saro/commons/

## see
- [가리사니 개발자공간](https://gs.saro.me) (only korean language)

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
Utils.openZipFromFile(File, ThrowableTriConsumer<String, ZipEntry, InputStream>)
Utils.openZipFromWeb(Web, ThrowableTriConsumer<String, ZipEntry, InputStream>)
Utils.openZipStreamNotClose(InputStream, ThrowableTriConsumer<String, ZipEntry, InputStream>)
Utils.random(long, long)
```

## Valids
#### me.saro.commons.Valids
```
Valids.isMail(String, int)
```

## Lambdas
#### me.saro.commons.lambdas.Lambdas
```
Lambdas.runtime(ThrowableBiConsumer<T, U>)
Lambdas.runtime(ThrowableBiFunction<T, U, R>)
Lambdas.runtime(ThrowableConsumer<T>)
Lambdas.runtime(ThrowableFunction<T, R>)
Lambdas.runtime(ThrowableRunnable)
Lambdas.runtime(ThrowableSupplier<R>)
```

# INSTANCE

## Web
#### me.saro.commons.web.Web

```
custom(String, String)
delete(String)
get(String)
patch(String)
post(String)
put(String)
addUrlParameter(String, String)
readRawResultStream(ThrowableConsumer<InputStream>)
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

## DateFormat
#### me.saro.commons.DateFormat
```
format(String, String, String)
now()
parse(Date)
parse(long)
parse(String, String)
valid(String, String)
calendar : Calendar
addDates(int)
addHours(int)
addMilliseconds(int)
addMinutes(int)
addMonth(int)
addYear(int)
clone()
equals(Object)
format(String)
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
```

## JsonReader
#### me.saro.commons.JsonReader
```
create(String)
emptyList()
emptyObject()
clone()
equals(Object)
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
```


# INTERFACE
#### me.saro.commons.lambdas.*
```
ThrowableRunnable
ThrowableSupplier<T>
ThrowableConsumer<T>
ThrowableBiConsumer<T, U>
ThrowableTriConsumer<T, U, V>
ThrowableFunction<T, R>
ThrowableBiFunction<T, U, R>
ThrowableTriFunction<T, U, V, R>
```
