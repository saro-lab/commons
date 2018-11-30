### saro commons
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/me.saro/commons/badge.svg)](https://maven-badges.herokuapp.com/maven-central/me.saro/commons)
[![GitHub license](https://img.shields.io/github/license/saro-lab/commons.svg)](https://github.com/saro-lab/commons/blob/master/LICENSE)


# QUICK START

## maven

``` xml
<dependency>
  <groupId>me.saro</groupId>
  <artifactId>commons</artifactId>
  <version>1.3</version>
</dependency>
```

## gradle

```
compile 'me.saro:commons:1.3'
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
Converter.asList(T[]) : List<T>
Converter.namingConvention(NamingConvention, List<String>) : String
Converter.namingConvention(NamingConvention, NamingConvention, String) : String
Converter.namingConvention(NamingConvention, String) : List<String>
Converter.splitCsvLine(String) : String[]
Converter.toByteArrayOutputStream(InputStream, int) : ByteArrayOutputStream
Converter.toBytes(InputStream, int) : byte[]
Converter.toClassByJson(String, TypeReference<T>) : T
Converter.toHash(HashAlgorithm, String) : byte[]
Converter.toHash(HashAlgorithm, String, String) : byte[]
Converter.toHash(HashAlgorithm, byte[]) : byte[]
Converter.toHashHex(HashAlgorithm, String) : String
Converter.toHashHex(HashAlgorithm, String, String) : String
Converter.toJson(Object) : String
Converter.toList(Enumeration<T>) : List<T>
Converter.toList(Iterable<T>) : List<T>
Converter.toMap(Object[]) : Map<K, V>
Converter.toMapByJsonObject(String) : Map<String, Object>
Converter.toMapListByJsonArray(String) : List<Map<String, Object>>
Converter.toStream(Enumeration<T>) : Stream<T>
Converter.toStream(Enumeration<T>, boolean) : Stream<T>
Converter.toStream(Iterable<T>) : Stream<T>
Converter.toStream(Iterable<T>, boolean) : Stream<T>
Converter.toStreamByResultSet(ResultSet, ThrowableBiFunction<String[], Object[], R>) : Stream<R>
Converter.toStreamByResultSet(ResultSet, ThrowableFunction<ResultSet, R>) : Stream<R>
Converter.toStreamLineNotCloseByTextInputStream(InputStream, String) : Stream<String>
Converter.toString(Exception) : String
Converter.toString(File, String) : String
Converter.toString(InputStream, String) : String
Converter.toStringNotClose(InputStream, String) : String
```


## Bytes

#### me.saro.commons.bytes.Bytes

```
Bytes.toBytes(double) : byte[]
Bytes.toBytes(float) : byte[]
Bytes.toBytes(int) : byte[]
Bytes.toBytes(long) : byte[]
Bytes.toBytes(short) : byte[]
Bytes.toDouble(byte[]) : double
Bytes.toDouble(byte[], int) : double
Bytes.toFloat(byte[]) : float
Bytes.toFloat(byte[], int) : float
Bytes.toHex(byte[]) : String
Bytes.toInt(byte[]) : int
Bytes.toInt(byte[], int) : int
Bytes.toLong(byte[]) : long
Bytes.toLong(byte[], int) : long
Bytes.toShort(byte[]) : short
Bytes.toShort(byte[], int) : short
```


## Utils

#### me.saro.commons.Utils

```
Utils.createRandomBase62String(int, int) : String
Utils.createRandomString(char[], int) : String
Utils.createRandomString(char[], int, int) : String
Utils.evl(String[]) : String
Utils.executeAllThreads(ExecutorService, List<T>, ThrowableFunction<T, R>) : List<R>
Utils.executeAllThreads(int, List<T>, ThrowableFunction<T, R>) : List<R>
Utils.inputStreamLineReader(InputStream, String, ThrowableFunction<Stream<String>, T>) : T
Utils.inputStreamReader(InputStream, StreamReadConsumer) 
Utils.kill(Closeable) 
Utils.kill(Thread) 
Utils.nvl(T[]) : T
Utils.random(long, long) : long
Utils.timerTask(ThrowableConsumer<TimerTask>) : TimerTask
```


## Files

#### me.saro.commons.Files

```
Files.attributesFilter(ThrowablePredicate<BasicFileAttributes>) : Predicate<File>
Files.createFile(File, boolean, InputStream) 
Files.lineReader(File, String, ThrowableFunction<Stream<String>, T>) : T
Files.streamFiles(File) : Stream<File>
Files.streamFiles(String) : Stream<File>
Files.toBasicFileAttributes(File) : BasicFileAttributes
```


## Zips

#### me.saro.commons.Zips

```
Zips.openFromFile(File, ThrowableTriConsumer<String, ZipEntry, InputStream>) 
Zips.openFromWeb(Web, ThrowableTriConsumer<String, ZipEntry, InputStream>) 
Zips.openStreamNotClose(InputStream, ThrowableTriConsumer<String, ZipEntry, InputStream>) 
```


## Valids

#### me.saro.commons.Valids

```
Valids.isMail(String, int) : boolean
```


## Tests

#### me.saro.commons.Tests

```
Tests.timestamp(ThrowableRunnable) : long
```

# INSTANCE


## DataFormat

#### me.saro.commons.bytes.DataFormat

```
DataFormat.createFixedData(Class<T>) : DataFormat<T>
DataFormat.createFixedData(Class<T>, Supplier<T>) : DataFormat<T>
bindBytes(OutputStream, T) 
bindBytes(byte[], int, T) 
toBytes(T) : byte[]
toClass(byte[]) : T
toClass(byte[], int) : T
toClassWithCheckSize(String) : T
toClassWithCheckSize(String, String) : T
toClassWithCheckSize(byte[]) : T
```


## Web

#### me.saro.commons.web.Web

```
Web.custom(String, String) : Web
Web.delete(String) : Web
Web.get(String) : Web
Web.patch(String) : Web
Web.post(String) : Web
Web.put(String) : Web
addUrlParameter(String, String) : Web
readRawResultStream(ThrowableConsumer<InputStream>) : WebResult<String>
saveFile(File, boolean) : WebResult<File>
setContentType(String) : Web
setHeader(String, String) : Web
setIgnoreCertificate(boolean) : Web
setRequestCharset(String) : Web
setResponseCharset(String) : Web
toCustom(ThrowableFunction<InputStream, R>) : WebResult<R>
toCustom(WebResult<R>, ThrowableFunction<InputStream, R>) : WebResult<R>
toJsonReader() : WebResult<JsonReader>
toJsonTypeReference(TypeReference<T>) : WebResult<T>
toMapByJsonObject() : WebResult<Map<String, Object>>
toMapListByJsonArray() : WebResult<List<Map<String, Object>>>
toPlainText() : WebResult<String>
writeBody(String) : Web
writeBody(byte[]) : Web
writeBodyParameter(String, String) : Web
writeJsonByClass(Object) : Web
```


## FTP

#### me.saro.commons.ftp.FTP

```
FTP.openFTP(InetAddress, int, String, String) : FTP
FTP.openFTP(String, int, String, String) : FTP
FTP.openFTPS(InetAddress, int, String, String) : FTP
FTP.openFTPS(String, int, String, String) : FTP
FTP.openSFTP(String, int, String, String) : FTP
cd(String) : boolean
close() 
delete(String) : boolean
hasDirectory(String) : boolean
hasFile(String) : boolean
listDirectories() : List<String>
listDirectories(Predicate<String>) : List<String>
listFiles() : List<String>
listFiles(Predicate<String>) : List<String>
mkdir(String) : boolean
path() : String
path(String) : boolean
pwd() : String
recv(List<String>, File) 
recv(String, File) : boolean
send(File) : boolean
send(String, File) : boolean
```


## SSHShell

#### me.saro.commons.ssh.SSHShell

```
SSHShell.open(String, int, String, String, String, ThrowableConsumer<String>) : SSHShell
close() 
cmd(String[]) 
cmdExitAndJoinEOF() 
isConnected() : boolean
joinEOF() 
```


## SSHExecutor

#### me.saro.commons.ssh.SSHExecutor

```
SSHExecutor.just(String, int, String, String, String, String[]) : String
SSHExecutor.open(String, int, String, String, String) : SSHExecutor
close() 
cmd(String[]) : String
```


## DateFormat

#### me.saro.commons.DateFormat

```
DateFormat.format(String, String, String) : String
DateFormat.now() : DateFormat
DateFormat.parse(Date) : DateFormat
DateFormat.parse(String, String) : DateFormat
DateFormat.parse(long) : DateFormat
DateFormat.valid(String, String) : boolean
addDates(int) : DateFormat
addHours(int) : DateFormat
addMilliseconds(int) : DateFormat
addMinutes(int) : DateFormat
addMonth(int) : DateFormat
addYear(int) : DateFormat
clone() : DateFormat
clone() : Object
equals(Object) : boolean
format(String) : String
getDate() : int
getDayOfWeek() : int
getHours() : int
getMilliseconds() : int
getMinute() : int
getMonth() : int
getSeconds() : int
getTimeInMillis() : long
getWeekOfMonth() : int
getWeekOfYear() : int
getYear() : int
setDate(int) : DateFormat
setHours(int) : DateFormat
setMilliseconds(int) : DateFormat
setMinutes(int) : DateFormat
setMonth(int) : DateFormat
setSeconds(int) : DateFormat
setTimeInMillis(long) : DateFormat
setYear(int) : DateFormat
toDate() : Date
toISO8601() : String
toString() : String
toString(String) : String
```


## JsonReader

#### me.saro.commons.JsonReader

```
JsonReader.create(String) : JsonReader
JsonReader.emptyList() : JsonReader
JsonReader.emptyObject() : JsonReader
equals(Object) : boolean
get(String) : Object
get(int) : JsonReader
getInt(String, int) : int
getString(String) : String
into(String) : JsonReader
isArray() : boolean
isObject() : boolean
length() : int
toList() : List<JsonReader>
toString() : String
```

# FUNCTION INTERFACE
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
