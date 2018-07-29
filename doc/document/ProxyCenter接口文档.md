#使用前必读
#### 1.0 本文档所有接口应答均为如下格式：

  
```json
{
    "data": 6,
    "message": null
}
```
#### 1.1 请求参数说明
+ 1 登录和注册接口参照具体的接口说明传参
+ 2 

#### 1.2 应答参数说明

参数名称|类型|说明
:---:|:---:|:---:
data|Object|业务执行结果 参照具体应答的参数说明
message|String|业务执行失败时的错误信息

#### 1.3 注意
+ 1.本接口文档的所有接口均为Http请求格式，在使用接口时，应当先判断http返回码
+ 2.仅当http返回码为200，说明业务处理成功。此时应关注data字段返回的业务数据
+ 3.当http返回码为其他时，说明业务处理失败。此时应关注message字段给出的错误提示
##1.登录注册
### 1.1 用户名登录
+ 接口名称：用户名登录
+ 接口地址：/logonWithUserName
+ 请求方式：POST
+ 请求

参数名称|类型|说明|可否为空
:---:|:---:|:---:|:---:
username|String|用户名|notNull
password|String|用户密码|notNull

+ 应答

参数|类型|说明
:---:|:---:|:---:
data|int|用户ID

+ 示例
```json
{
    "data": 6
}
```
### 1.2 手机号登录
+ 接口名称：手机号登录
+ 接口地址：/logonWithPhone
+ 请求方式：POST
+ 请求

参数名称|类型|说明|可否为空
:---:|:---:|:---:|:---:
phone|String|手机号|notNull
password|String|用户密码|notNull

+ 应答

参数|类型|说明
:---:|:---:|:---:
data|int|用户ID

+ 示例
```json
{
    "data": 6
}
```
### 1.3 邮箱登录
+ 接口名称：邮箱登录
+ 接口地址：/logonWithEmail
+ 请求方式：POST
+ 请求

参数名称|类型|说明|可否为空
:---:|:---:|:---:|:---:
email|String|邮箱|notNull
password|String|用户密码|notNull

+ 应答

参数|类型|说明
:---:|:---:|:---:
data|int|用户ID

+ 示例
```json
{
    "data": 6
}
```
### 1.4 注册
+ 接口名称：邮箱登录
+ 接口地址：/register
+ 请求方式：POST
+ 请求(userName/phone/email不可同时为空)

参数名称|类型|说明|可否为空
:---:|:---:|:---:|:---:
userName|String|用户名|null
phone|String|手机号|null
email|String|邮箱|null
password|String|用户密码|notNull

+ 应答

参数|类型|说明
:---:|:---:|:---:
data|int|用户ID

+ 示例
```json
{
    "data": 8
}
```
##用户基本信息管理
### 2.1 查询用户自己的基本信息
+ 接口名称：查询用户基本信息
+ 接口地址：/user
+ 请求方式：GET
+ methodName:USER_MANAGER_USER_INFO_GET


+ parameters:null

+ 应答

参数|类型|说明
:---:|:---:|:---:
id|int|用户ID|用户ID
nickName|String|昵称
birthday|Date|生日
gender|String|性别 0:男 1:女
address|String|地址
signature|String|签名
userPortrait|String|头像Url
email|String|邮箱
phone|String|手机号
createTime|Date|创建时间
mask|int|掩码值

+ 示例
```json
{
    "data": {
        "id": 10,
        "nickName": null,
        "birthday": null,
        "gender": 0,
        "address": null,
        "signature": null,
        "userPortrait": null,
        "email": "mamian@com",
        "phone": "18911927890",
        "createTime": "2018-07-27T07:13:29.000+0000",
        "mask": null
    }
}
```