
namespace java com.jd.si.person.user.thrift


struct User {
       1:string name
       2:i32 age
}

service UserService {
    User get()
}