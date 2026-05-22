[감정 공유 플랫폼]
-로그인/ 회원가입, 댓글, 검색
-감정에 따른 위로 문구 출력
-일주일간의 감정 변화 통계 출력

***URL Mapping***

**[HomeController]**

/ -> Home  

/join -> Join 

/login -> Login 

/search -> Search

**[UserController]**

/users/{id} -> See Profile 

/users/logout -> Log Out 

/users/edit -> Edit My Profile 

/users/delete -> Delete My Profile

**[ActicleController]**

/articles → See All articles

/articles/{id} -> See article 

/articles/{id}/edit -> Edit article

/articles/{id}/delete -> Delete article

/articles/create -> Create article

***REST API Controller***

**[EmotionApiController]**

/api/emotions → POST
/api/emotions/recent → GET
/api/emotions/statistics → GET

**[AritcleApiController]**

/api/articles/{id}/comment → POST, DELETE

/api/articles/{id}/view → POST
