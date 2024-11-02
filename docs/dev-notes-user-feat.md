# Dev Notes

## 1.1 java and spring boot version in maven

### java version : maven and idea config

```xml
<properties>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <java.version>1.8</java.version>
</properties>

<plugin>
    <artifactId>maven-compiler-plugin</artifactId>
    <configuration>
        <source>1.8</source>
        <target>1.8</target>
    </configuration>
</plugin>
```

### SpringBoot3.x only support java 1.17+

## 1.2 UserInfo Module

UserInfo = User + Role

### 1.2.1 domain

User Many-To-Many Role

repository

GrantAuthority Comparator

password encryption


## ToDO Lists

- UserDetails Page
- After registering redirect userDetail