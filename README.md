# # STATIC

## Converter

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

```
Utils.evl(String...)
Utils.nvl(T...)
Utils.openZipFromFile(File, ThrowableTriConsumer<String, ZipEntry, InputStream>)
Utils.openZipFromWeb(Web, ThrowableTriConsumer<String, ZipEntry, InputStream>)
Utils.openZipStreamNotClose(InputStream, ThrowableTriConsumer<String, ZipEntry, InputStream>)
Utils.random(long, long)
```

## Valids

```
Valids.isMail(String, int)
```

## Lambdas

```
Lambdas.runtime(ThrowableBiConsumer<T, U>)
Lambdas.runtime(ThrowableBiFunction<T, U, R>)
Lambdas.runtime(ThrowableConsumer<T>)
Lambdas.runtime(ThrowableFunction<T, R>)
Lambdas.runtime(ThrowableRunnable)
Lambdas.runtime(ThrowableSupplier<R>)
```

# # INSTANCE

## DateFormat

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

## Web

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


# # INTERFACE

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
