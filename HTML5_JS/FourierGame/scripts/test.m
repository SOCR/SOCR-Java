clc;clear;close all;
M1 = 1;
M2 = 2;
S1 = 1;
S2 = 0.5;
R = 0.2;
out = [];
i = 1;
j = 1;
for X = -4:0.2:6
    j = 1;
    for Y = -0.5:0.1:4.5
        h = -(X-M1)/S1;
        k=  -(Y-M2)/S2;
        T=1/(1+.2316419*abs(h));
        D=0.3989423*exp(-h*h/2);
        Prob=D*T*(.3193815+T*(-0.3565638+T*(1.781478+T*(-1.821256+T*1.330274))));
        T1=1./(1+.2316419.*abs(k));
        D1=0.3989423*exp(-k.*k/2);
        Prob1=D1*T1*(.3193815+T1*(-0.3565638+T1*(1.781478+T1*(-1.821256+T1*1.330274))));
        if (h>0)
            Prob = 1-Prob;
        end
        if (k > 0)
            Prob1= 1-Prob1;
        end
        s=(1-Prob1)*(1-Prob);
        sqr2pi=sqrt(2*pi);
        h0=exp(-h*h./2)./sqr2pi;
        k0=exp(-k*k./2)./sqr2pi;
        h1=-h.*h0;
        k1=-k.*k0;
        factor=R*R/2;
        s=s+R*h0*k0+factor*h1*k1;
        n=2;
        while ((n*(1-abs(R))<5)&&(n<101))
            factor=factor*R/(n+1);
            h2=-X*h1-(n-1)*h0;
            k2=-Y*k1-(n-1)*k0;
            s=s+factor*h2*k2;
            h0=h1;
            k0=k1;
            h1=h2;
            k1=k2;
            n=n+1;
        end
        v=0;
        if(R>0.95)
            w = max([h k]);
            T2=1/(1+.2316419*abs(w));
            D2=0.3989423*exp(-w*w/2);
            Prob2=D2*T2*(.3193815+T2*(-0.3565638+T2*(1.781478+T2*(-1.821256+T2*1.330274))));
            v = 1-Prob2;
            s=v+20*(s-v)*(1-R);
        elseif ((R<-.95)&&(h+k<0))
            v = abs(Prob1-Prob2);
            s = v+20*(s-v)*(1+R);
        end
        out(i,j) = s;
        j = j + 1;
    end
    i = i + 1;
end
moo = [];
for k = 1:51
    for m = 1:51
        if(k==1||m==1)
            moo(k,m) = 0;
        else
            moo(k,m)=(out(k,m)+out(k-1,m-1)-out(k,m-1)-out(k-1,m))/(0.2*0.1);
        end
    end
end
surf(moo);
figure;
surf(out);
sum(sum(moo))