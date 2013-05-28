#define _modA(m,mij)     Math.sqrt(m##Re##mij*m##Re##mij+m##Im##mij*m##Im##mij)

#define _multZZRe(u,v)     (u.real()*v.real()-u.imag()*v.imag())
#define _multZZIm(u,v)     (u.imag()*v.real()+u.real()*v.imag())
#define _multAZRe(m,mij,z) (m##Re##mij*z.real()-m##Im##mij*z.imag())
#define _multAZIm(m,mij,z) (m##Im##mij*z.real()+m##Re##mij*z.imag())
#define _multAARe(m,mij,n,nij) (m##Re##mij*n##Re##nij-m##Im##mij*n##Im##nij)
#define _multAAIm(m,mij,n,nij) (m##Im##mij*n##Re##nij+m##Re##mij*n##Im##nij)
