### saro commons
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/me.saro/commons/badge.svg)](https://maven-badges.herokuapp.com/maven-central/me.saro/commons)
[![GitHub license](https://img.shields.io/github/license/saro-lab/commons.svg)](https://github.com/saro-lab/commons/blob/master/LICENSE)


# QUICK START

## maven

``` xml
<dependency>
  <groupId>me.saro</groupId>
  <artifactId>commons</artifactId>
  <version>4.0.3</version>
</dependency>
```

## gradle

```
compile 'me.saro:commons:4.0.3'
```

## repository
- https://search.maven.org/artifact/me.saro/commons
- http://central.maven.org/maven2/me/saro/commons/
- https://mvnrepository.com/artifact/me.saro/commons

## see
- [가리사니 개발자공간](https://gs.saro.me)





# STATIC


## Converter

#### me.saro.commons.__old.bytes.Converter

```
Converter.asList(T[]) : List<T>
Converter.splitByToken(String, String) : List<String>
Converter.splitCsvLine(String) : String[]
Converter.toByteArrayOutputStream(InputStream, int) : ByteArrayOutputStream
Converter.toBytes(InputStream, int) : byte[]
Converter.toClassByJson(String, TypeReference<T>) : T
Converter.toDoubleArray(List<Double>) : double[]
Converter.toFloatArray(List<Float>) : float[]
Converter.toHash(HashAlgorithm, String) : byte[]
Converter.toHash(HashAlgorithm, String, String) : byte[]
Converter.toHash(HashAlgorithm, byte[]) : byte[]
Converter.toHashHex(HashAlgorithm, String) : String
Converter.toHashHex(HashAlgorithm, String, String) : String
Converter.toIntArray(List<Integer>) : int[]
Converter.toJson(Object) : String
Converter.toList(Enumeration<T>) : List<T>
Converter.toList(Iterable<T>) : List<T>
Converter.toLongArray(List<Long>) : long[]
Converter.toMap(Object[]) : Map<K, V>
Converter.toMapByClass(Object) : Map<String, T>
Converter.toMapByJsonObject(String) : Map<String, Object>
Converter.toMapListByClassList(Object) : List<Map<String, T>>
Converter.toMapListByJsonArray(String) : List<Map<String, Object>>
Converter.toPrimitive(Byte[]) : byte[]
Converter.toPrimitive(Double[]) : double[]
Converter.toPrimitive(Float[]) : float[]
Converter.toPrimitive(Integer[]) : int[]
Converter.toPrimitive(Long[]) : long[]
Converter.toPrimitive(Short[]) : short[]
Converter.toShortArray(List<Short>) : short[]
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
Converter.toString(URL, String) : String
Converter.toStringNotClose(InputStream, String) : String
Converter.toUnPrimitive(byte[]) : Byte[]
Converter.toUnPrimitive(double[]) : Double[]
Converter.toUnPrimitive(float[]) : Float[]
Converter.toUnPrimitive(int[]) : Integer[]
Converter.toUnPrimitive(long[]) : Long[]
Converter.toUnPrimitive(short[]) : Short[]
```


## Bytes

#### me.saro.commons.__old.bytes.Bytes

```
Bytes.copy(byte[], boolean) : byte[]
Bytes.copy(byte[], int, int, boolean) : byte[]
Bytes.decodeBase64(String) : byte[]
Bytes.decodeBase64(String, String) : String
Bytes.encodeBase64String(String, String) : String
Bytes.encodeBase64String(byte[]) : String
Bytes.reverse(byte[]) : byte[]
Bytes.reverse(byte[], boolean) : byte[]
Bytes.reverse(byte[], int, int) : byte[]
Bytes.reverse(byte[], int, int, boolean) : byte[]
Bytes.reverse(byte[], int, int, int) : byte[]
Bytes.toBytes(double) : byte[]
Bytes.toBytes(double[]) : byte[]
Bytes.toBytes(double[], int, int) : byte[]
Bytes.toBytes(float) : byte[]
Bytes.toBytes(float[]) : byte[]
Bytes.toBytes(float[], int, int) : byte[]
Bytes.toBytes(int) : byte[]
Bytes.toBytes(int[]) : byte[]
Bytes.toBytes(int[], int, int) : byte[]
Bytes.toBytes(long) : byte[]
Bytes.toBytes(long[]) : byte[]
Bytes.toBytes(long[], int, int) : byte[]
Bytes.toBytes(short) : byte[]
Bytes.toBytes(short[]) : byte[]
Bytes.toBytes(short[], int, int) : byte[]
Bytes.toBytesByHex(String) : byte[]
Bytes.toDouble(byte[]) : double
Bytes.toDouble(byte[], int) : double
Bytes.toDoubleArray(byte[], int, int) : double[]
Bytes.toDoubleList(byte[], int, int) : List<Double>
Bytes.toFloat(byte[]) : float
Bytes.toFloat(byte[], int) : float
Bytes.toFloatArray(byte[], int, int) : float[]
Bytes.toFloatList(byte[], int, int) : List<Float>
Bytes.toHex(byte[]) : String
Bytes.toInt(byte[]) : int
Bytes.toInt(byte[], int) : int
Bytes.toIntArray(byte[], int, int) : int[]
Bytes.toIntegerList(byte[], int, int) : List<Integer>
Bytes.toLong(byte[]) : long
Bytes.toLong(byte[], int) : long
Bytes.toLongArray(byte[], int, int) : long[]
Bytes.toLongList(byte[], int, int) : List<Long>
Bytes.toShort(byte[]) : short
Bytes.toShort(byte[], int) : short
Bytes.toShortArray(byte[], int, int) : short[]
Bytes.toShortList(byte[], int, int) : List<Short>
```


## Utils

#### me.saro.commons.__old.bytes.Utils

```
Utils.blank(String) : boolean
Utils.bvl(String[]) : String
Utils.createRandomBase62String(int, int) : String
Utils.createRandomString(char[], int) : String
Utils.createRandomString(char[], int, int) : String
Utils.evl(String[]) : String
Utils.executeAllThreads(ExecutorService, List<T>, ThrowableFunction<T, R>) : List<R>
Utils.executeAllThreads(int, List<T>, ThrowableFunction<T, R>) : List<R>
Utils.groupBy(List<T>, Function<? super T, ? extends K>) : Collection<List<T>>
Utils.inputStreamLineReader(InputStream, String, ThrowableFunction<Stream<String>, T>) : T
Utils.inputStreamReader(InputStream, StreamReadConsumer)
Utils.kill(Closeable)
Utils.kill(Thread)
Utils.linkStream(InputStream, OutputStream)
Utils.nel(Object, ThrowableRunnable)
Utils.nel(Object, ThrowableRunnable, ThrowableRunnable)
Utils.norNumber(String) : String
Utils.nvl(T[]) : T
Utils.parseDouble(String) : double
Utils.parseLong(String) : long
Utils.random(long, long) : long
Utils.sort(List<T>, Comparator<T>) : List<T>
Utils.timerTask(ThrowableConsumer<TimerTask>) : TimerTask
Utils.timestamp(ThrowableRunnable) : long
Utils.zerofill(String, int) : String
Utils.zerofill(int, int) : String
Utils.zerofill(long, int) : String
```


## Files

#### me.saro.commons.__old.bytes.Files

```
Files.attributesFilter(ThrowablePredicate<BasicFileAttributes>) : Predicate<File>
Files.createFile(File, boolean, InputStream)
Files.createFile(File, boolean, String, String)
Files.lineReader(File, String, ThrowableFunction<Stream<String>, T>) : T
Files.lineReader(String, String, ThrowableFunction<Stream<String>, T>) : T
Files.listFilesStream(File) : Stream<File>
Files.listFilesStream(String) : Stream<File>
Files.move(File, File, boolean) : boolean
Files.move(List<File>, String, boolean) : int
Files.toBasicFileAttributes(File) : BasicFileAttributes
Files.toFileExt(File) : String
Files.toFileExt(String) : String
Files.validFileExt(File, String[]) : boolean
Files.validFileExt(String, String[]) : boolean
```


## Maps

#### me.saro.commons.__old.bytes.Maps

```
Maps.clone(Map<K, V>) : Map<K, V>
Maps.filter(Map<K, V>, Predicate<Map$Entry<K, V>>) : Map<K, V>
Maps.pick(Map<K, V>, K[]) : Map<K, V>
Maps.toMap(Object[]) : Map<K, V>
```


## Zips

#### me.saro.commons.__old.bytes.Zips

```
Zips.openFromFile(File, ThrowableTriConsumer<String, ZipEntry, InputStream>)
Zips.openFromWeb(BasicWeb, ThrowableTriConsumer<String, ZipEntry, InputStream>)
Zips.openStreamNotClose(InputStream, ThrowableTriConsumer<String, ZipEntry, InputStream>)
```


## Valids

#### me.saro.commons.__old.bytes.Valids

```
Valids.isDate(String, String) : boolean
Valids.isMail(String, int) : boolean
Valids.isNotBlank(String[]) : boolean
Valids.isNotNull(Object[]) : boolean
```


## Naming

#### me.saro.commons.__old.bytes.Naming

```
Naming.toCamelCase(List<String>) : String
Naming.toDashes(List<String>) : String
Naming.toPascalCase(List<String>) : String
Naming.toUnderscores(List<String>) : String
Naming.toWordsByCamelCase(String) : List<String>
Naming.toWordsByDashes(String) : List<String>
Naming.toWordsByPascalCase(String) : List<String>
Naming.toWordsByUnderscores(String) : List<String>
```


## Shell

#### me.saro.commons.__old.bytes.shell.Shell

```
Shell.execute(String[]) : ShellResult
```

# INSTANCE


## FixedData

#### me.saro.commons.__old.bytes.fd.FixedData

```
FixedData.getInstance(Class<?>) : FixedData
bindBytes(Object, OutputStream) : OutputStream
bindBytes(Object, byte[]) : byte[]
bindBytes(Object, byte[], int) : byte[]
getTargetClass() : Class<?>
meta() : FixedDataClass
toBytes(Object) : byte[]
toClass(String) : T
toClass(byte[]) : T
toClass(byte[], int) : T
toClassWithCheckByte(String) : T
toString(Object) : String
```


## FTP

#### me.saro.commons.__old.bytes.ftp.FTP

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


## Crypt

#### me.saro.commons.__old.bytes.crypt.Crypt

```
Crypt.decrypt(String, byte[], byte[]) : Crypt
Crypt.encrypt(String, byte[], byte[]) : Crypt
to(File, File, boolean)
to(InputStream, OutputStream)
toBase64(byte[]) : String
toBase64(byte[], int, int) : String
toBase64ByBase64(String) : String
toBase64ByHex(String) : String
toBytes(byte[]) : byte[]
toBytes(byte[], int, int) : byte[]
toBytesByBase64(String) : byte[]
toBytesByHex(String) : byte[]
toHex(byte[]) : String
toHex(byte[], int, int) : String
toHexByBase64(String) : String
toHexByHex(String) : String
```


## SSHShell

#### me.saro.commons.__old.bytes.ssh.SSHShell

```
SSHShell.open(String, int, String, String, String, ThrowableConsumer<String>) : SSHShell
close()
cmd(String[])
cmdExitAndJoinEOF()
isConnected() : boolean
joinEOF()
```


## SSHExecutor

#### me.saro.commons.__old.bytes.ssh.SSHExecutor

```
SSHExecutor.just(String, int, String, String, String, String[]) : String
SSHExecutor.open(String, int, String, String, String) : SSHExecutor
close()
cmd(String[]) : String
```


## DateFormat

#### me.saro.commons.__old.bytes.DateFormat

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
toCalendar() : Calendar
toDate() : Date
toISO8601() : String
toString() : String
toString(String) : String
```


## JsonReader

#### me.saro.commons.__old.bytes.json.JsonReader

```
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


## ByteData

#### me.saro.commons.__old.bytes.ByteData

```
ByteData.create() : ByteData
ByteData.create(String) : ByteData
ByteData.create(int, String) : ByteData
bind(OutputStream) : ByteData
bind(OutputStream, int) : ByteData
fillSpace() : ByteData
insert(String, int) : ByteData
insert(byte[], int) : ByteData
insert(byte[], int, int, int) : ByteData
insertFill(byte, int, int) : ByteData
insertFillSpace(int, int) : ByteData
insertFixed(String, int, byte, int) : ByteData
insertFixedAlignRight(String, int, byte, int) : ByteData
moveWritePointer(int) : ByteData
newByteData(int, int) : ByteData
read(int) : byte[]
readIgnore(int) : ByteData
readIgnoreCurrentLine() : ByteData
readIgnoreMatch(byte) : ByteData
readText(int, boolean) : String
readTextAlignRight(int, boolean) : String
readTextInt(int, int) : int
readTextLong(int, long) : long
rectifyWritePointer() : ByteData
size() : int
toBytes() : byte[]
toBytes(int, int) : byte[]
toString() : String
write(File) : ByteData
write(File, String) : ByteData
write(InputStream) : ByteData
write(InputStream, int) : ByteData
write(String) : ByteData
write(byte[]) : ByteData
write(byte[], int, int) : ByteData
writeFill(byte, int) : ByteData
writeFillSpace(int) : ByteData
writeFixed(String, int, byte) : ByteData
writeFixed(int, int, byte) : ByteData
writeFixedAlignRight(String, int, byte) : ByteData
writeFixedAlignRight(int, int, byte) : ByteData
writeLine1() : ByteData
writeLine2() : ByteData
```


## Web

#### me.saro.commons.__old.bytes.web.Web

```
Web.custom(String, String) : Web
Web.delete(String) : Web
Web.get(String) : Web
Web.patch(String) : Web
Web.post(String) : Web
Web.put(String) : Web
addUrlParameter(String, String) : Web
getRequestCharset() : String
getResponseCharset() : String
readRawResultStream(ThrowableConsumer<InputStream>) : WebResult<String>
saveFile(File, boolean) : WebResult<File>
setConnectTimeout(int) : Web
setContentType(String) : Web
setContentTypeApplicationJson() : Web
setHeader(String, String) : Web
setIgnoreCertificate(boolean) : Web
setReadTimeout(int) : Web
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


## NullOutputStream

#### me.saro.commons.__old.bytes.NullOutputStream

```
write(int)
```

# FUNCTION INTERFACE
#### me.saro.commons.__old.bytes.function.*
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
