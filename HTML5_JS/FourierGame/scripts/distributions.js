/*
JavaScript file for Distributome project
See http://www.distributome.org
*/

//Special Functions

//Tests for parity
function isEven(n){
    if (n % 2 === 0) return true;
	else return false;
}

function isOdd(n){
	if ((n - 1) % 2 === 0) return true;
	else return false;
}

//Sign functon
function sgn(x){
	if (x > 0) return 1;
	else if (x < 0) return -1;
	else return 0;
}

//Sorting functions
function ascend(a, b){
	return a - b;
}

function descend(a, b){
	return b - a;
}

//Generalzied power function
function genPow(a, n, b){
	var product = 1;
	for (var i = 0; i < n; i++)	product = product * (a + i * b);
	return product;
}

//Rising power funtion
function risePow(a, n){
	return genPow(a, n, 1);
}

//Falling power function
function perm(n, k){
	var product = 1;
	for (var i = 0; i < k; i++)	product = product * (n - i);
	return product;
}

//Factorial function
function factorial(n){
	return perm(n, n);
}

//Binomial coefficient
function binomial(n, k){
	var product = 1;
	for (var i = 0; i < k; i++)	product = product * ((n - i) / (k - i));
	return product;
}

//Polylogarithm function
function polyLog(a, x){
	var sum = 0, k = 1, e = 0.0001;
	while (Math.pow(x, k) / Math.pow(k, a) > e){
		sum = sum + Math.pow(x, k) / Math.pow(k, a);
		k++;
	}
	return sum;
}

//Sampling functions
function sample(p, n, type){
	var m = p.length;
	var t, k, u;
	var s = new Array(n);
	if (type == 1){
		for (var i = 0; i < n; i++){
			u = Math.floor(m * Math.random());
			s[i] = p[u];
		}
	}
	else{
		for (var j = 0; j < n; j++){
			k = m - j;
			u = Math.floor(k * Math.random());
			s[j] = p[u];
			t = p[k - 1];
			p[k - 1] = p[u];
			p[u] = t;
		}
	}
	return s;
}

//Log gamma function
function logGamma(x){
	var coef = [76.18009173, -86.50532033, 24.01409822, -1.231739516, 0.00120858003, -0.00000536382];
	var step = 2.50662827465, fpf = 5.5, t, tmp, ser;
	t = x - 1;
	tmp = t + fpf;
	tmp = (t + 0.5) * Math.log(tmp) - tmp;
	ser = 1;
	for (var i = 1; i <= 6; i++){
		t = t + 1;
		ser = ser + coef[i - 1] / t;
	}
	return tmp + Math.log(step * ser);
}

//Gamma function
function gamma(x){
	return Math.exp(logGamma(x));
}

//Gamma series function
function gammaSeries(x, a){
	var maxit = 100, eps = 0.0000003;
	var sum = 1 / a, ap = a, gln = logGamma(a), del = sum;
	for (var n = 1; n <= maxit; n++){
		ap++;
		del = del * x / ap;
		sum = sum + del;
		if (Math.abs(del) < Math.abs(sum) * eps) break;
	}
	return sum * Math.exp(-x + a * Math.log(x) - gln);
}

//Gamma continued fraction function
function gammaCF(x, a){
	var maxit = 100, eps = 0.0000003;
	var gln = logGamma(a), g = 0, gOld = 0, a0 = 1, a1 = x, b0 = 0, b1 = 1, fac = 1;
	var an, ana, anf;
	for (var n = 1; n <= maxit; n++){
		an = 1.0 * n;
		ana = an - a;
		a0 = (a1 + a0 * ana) * fac;
		b0 = (b1 + b0 * ana) * fac;
		anf = an * fac;
		a1 = x * a0 + anf * a1;
		b1 = x * b0 + anf * b1;
		if (a1 !== 0){
			fac = 1.0 / a1;
			g = b1 * fac;
			if (Math.abs((g - gOld) / g) < eps) break;
			gOld = g;
		}
	}
	return Math.exp(-x + a * Math.log(x) - gln) * g;
}

//Gamma CDF
function gammaCDF(x, a){
	if (x <= 0) return 0;
	else if (x < a + 1) return gammaSeries(x, a);
	else return 1 - gammaCF(x, a);
}

//Beta continued fraction function
	function betaCF(x, a, b){
	var maxit = 100, eps = 0.0000003, am = 1, bm = 1, az = 1, qab = a + b, qap = a + 1, qam = a - 1, bz = 1 - qab * x / qap, tem, em, d, bpp, bp, app, aOld, ap;
	for (var m = 1; m <= maxit; m++){
		em = m;
		tem = em + em;
		d = em * (b - m) * x / ((qam + tem) * (a + tem));
		ap = az + d * am;
		bp = bz + d * bm;
		d = -(a + em) *(qab + em) * x / ((a + tem) * (qap + tem));
		app = ap + d * az;
		bpp = bp + d * bz;
		aOld = az;
		am = ap / bpp;
		bm = bp / bpp;
		az = app / bpp;
		bz = 1;
		if (Math.abs(az - aOld) < eps * Math.abs(az)) break;
	}
	return az;
}

//Beta CDF
function betaCDF(x, a, b){
	var bt;
	if ((x === 0) || (x === 1)) bt = 0;
	else bt = Math.exp(logGamma(a + b) - logGamma(a) - logGamma(b) + a * Math.log(x) + b * Math.log(1 - x));
	if (x < (a + 1) / (a + b + 2)) return bt * betaCF(x, a, b) / a;
	else return 1 - bt * betaCF(1 - x, b, a) / b;
}

//Zeta function
function zeta(x){
	var terms = Math.ceil(Math.pow(10, 4 / x));
	var sum = 0;
	for (var n = 1; n < terms; n++)
	sum = sum + 1 / Math.pow(n, x);
	return sum;
}

//Data distribution
function Data(a, b, s){
	this.lowerValue = a;
	this.upperValue = b;
	this.step = s;
	var size = 0, sum = 0, sumSquares = 0, minValue, maxValue;
	var n = Math.round((this.upperValue - this.lowerValue) / this.step) + 1;
	var freq = new Array(n);
	for(var i = 0; i < n; i++) freq[i] = 0;
	
	this.setValue = function(x){
		size++;
		sum = sum + x;
		sumSquares = sumSquares + x * x;
		if (x < minValue) minValue = x;
		if (x > maxValue) maxValue = x;
		freq[this.index(x)]++;
	};
	
	this.index = function(x){
		return Math.round((x - this.lowerValue) / this.step);
	};
	
	this.mean = function(){
		return sum / size;
	};
	
	this.variance = function(){
		return sumSquares / (size - 1) - sum * sum /(size * (size - 1));
	};
	
	this.stdDev = function(){
		return Math.sqrt(this.variance());
	};
	
	this.getFreq = function(x){
		return freq[this.index(x)];
	};
	
	this.relFreq = function(x){
		return freq[this.index(x)] / size;
	};
	
	this.density = function(x){
		return this.getFreq(x) / (size * this.step);
	};
	
	this.getSize = function(){
		return size;
	};
	
	this.reset = function(){
		sum = 0; sumSquares = 0; size = 0;
		minValue = this.upperValue; maxValue = this.lowerValue;
		for(i = 0; i < n; i++) freq[i] = 0;
	};
}

function CompleteData(){
	var values = new Array(), sum = 0, sum2 = 0;
	
	this.setValue = function(x){
		values.push(x);
		sum = sum + x;
		sum2 = sum2 + x * x;
	};
	
	this.getValue = function(i){
		if (0 <= i && i < values.length)
		return values[i];
	};
	
	this.getValues = function(){
		return values;
	};
	
	this.points = function(){
		return values.length;
	};
	
	this.mean = function(){
		return sum / this.points();
	};
	
	this.varianceP = function(){
		return sum2 / this.points() - Math.pow(this.mean(), 2);
	};
	
	this.stdDevP = function(){
		return Math.sqrt(this.varianceP());
	};
	
	this.variance = function(){
		var n = this.points();
		return (n / (n - 1)) * this.varianceP();
	};
	
	this.stdDev = function(){
		return Math.sqrt(this.variance());
	};
	
	this.reset = function(){
		values = new Array();
		sum = 0; 
		sum2 = 0;
	};
	
	this.orderStatistic = function(i){
		var sortedValues = values;
		sortedValues.sort(ascend)
		return sortedValues[i - 1];
	};
	
	this.quantile = function(p){
		var n = this.points(), rank = (n - 1) * p + 1, k = Math.floor(rank), t = rank - k;
		if (k == n) return this.orderStatistic(k);
		else return this.orderStatistic(k) + t * (this.orderStatistic(k + 1) - this.orderStatistic(k));
	};
	
	this.minValue = function(){
		return this.orderStatistic(1);
	};
	
	this.maxValue = function(){
		return this.orderStatistic(this.points());
	};
	
	this.freq = function(a, b){
		var count = 0;
		for (var i = 0; i < values.length; i++) if (a <= values[i] && values[i] <= b) count++;
		return count;
	};
	
	this.relFreq = function(a, b){
		return this.freq(a, b) / this.points();
	};
	
	this.density = function(a, b){
		return this.relFreq(a, b) / (b - a);
	};
}
	
//Generic probability distribution
function Distribution(minValue, maxValue, step, type, pdf){
	this.minValue = minValue || 0;
	this.maxValue = maxValue || 1;
	this.step = step || 1;
	this.type = type || 0;
	this.data = new Data(this.minValue, this.maxValue, this.step);
	
	this.density = function(x){
		var i = Math.round((x - this.minValue) / this.step);
		if (i < 0 || i >= pdf.length) return 0;
		else return pdf[i];
	};

	this.mode = function(){
		var x0 = this.minValue, y0 = this.density(x0), y;
		for (var x = this.minValue; x <= this.maxValue; x = x + this.step){
			y = this.density(x);
			if (y > y0){
				y0 = y;
				x0 = x;
			}
		}
		return x0;
	};
		
	this.maxDensity = function(){
		return this.density(this.mode());
	};
	
	this.CDF = function(x){
		var sum = 0, dt;
		if (type === 0) dt = 1; else dt = this.step;
		for (var t = this.minValue; t <= x; t = t + this.step) sum = sum + this.density(t) * dt;
		return sum;
	};
	
	this.quantile = function(p){
		var x, q;
		if (p < 0 || p > 1) return NaN;
		else if (p === 0) return this.minValue;
		else if (p === 1) return this.maxValue;
		else{
			x = this.minValue;
			q = this.CDF(x);
			while(q < p){
				x = x + this.step;
				q = this.CDF(x);
			}
			return x;
		}
	};
	
	this.moment = function(n, a){
		var sum = 0, dx;
		if (this.type === 0) dx = 1; else dx = this.step;
		for (var x = this.minValue; x <= this.maxValue; x = x + this.step) sum = sum + Math.pow((x - a), n) * this.density(x) * dx;
		return sum;
	};
	
	this.rawMoment = function(n){
		return this.moment(n, 0);
	};

	this.mean = function(){
		return this.rawMoment(1);
	};
	
	this.centralMoment = function(n){
		return this.moment(n, this.mean());
	};
		
	this.variance = function(){
		return this.centralMoment(2);
	};
		
	this.stdDev = function(){
		return Math.sqrt(this.variance());
	};
	
	this.skew = function(){
		var s = this.stdDev();
		return this.centralMoment(3) / Math.pow(s, 3);
	};
	
	this.kurt = function(){
		var s = this.stdDev();
		return this.centralMoment(4) / Math.pow(s, 4);
	};
	
	
	this.MGF = function(t){
		var sum = 0, dx;
		if (this.type === 0) dx = 1; else dx = this.step;
		for (var x = this.minValue; x <= this.maxValue; x = x + this.step) sum = sum + Math.exp(t * x) * this.density(x) * dx;
		return sum;
	};
	
	this.PGF = function(t){
		var sum = 0, dx;
		if (this.type === 0) dx = 1; else dx = this.step;
		for (var x = this.minValue; x <= this.maxValue; x = x + this.step) sum = sum + Math.pow(t, x) * this.density(x) * dx;
		return sum;
	};
	
	this.median = function(){
		return this.quantile(0.5);
	};
	
	this.quartile = function(i){
		if (i == 1) return this.quantile(0.25);
		else if (i == 2) return this.quantile(0.5);
		else if (i == 3) return this.quantile(0.75);
		else return NaN;
	};

	this.simulate = function(){
		var p = Math.random(), sum = 0, y;
		if (type === 0){
			for (var x = this.minValue; x <= this.maxValue; x = x + this.step){
				if ((sum < p) && (p <= sum + this.density(x))) y = x;
				sum = sum + this.density(x);
			}
		}
		else y = this.quantile(p);
		this.setValue(y);
		return y;
	};
	
	this.setValue = function(x){
		this.data.setValue(x);
	};	
}

//The binomial distribution
function BinomialDistribution(trials, prob){
	if (prob < 0) prob = 0;
	if (prob > 1) prob = 1;
	this.prob = prob;
	this.trials = trials;
	this.type = 0;
	this.minValue = 0;
	this.maxValue = this.trials;
	this.step = 1;
	this.data = new Data(0, this.trials, this.step);
	
	this.density = function(x){
		var k = Math.round(x);
		if (k < 0 || k > this.trials) return 0;
		else return binomial(this.trials, k) * Math.pow(this.prob, k) * Math.pow(1 - this.prob, this.trials - k);
	};
	
	this.mode = function(){
		if (prob == 1) return this.trials;
		else return Math.floor((this.trials + 1) * this.prob);
	};
			
	this.mean = function(){
		return this.trials * this.prob;
	};
	
	this.variance = function(){
		return this.trials * this.prob * (1 - this.prob);
	};
		
	this.simulate = function(){
		var successes = 0;
		for (var i = 1; i <= this.trials; i++){
			if (Math.random() < this.prob) successes++;
		}
		this.setValue(successes);
		return successes;
	};
	
	this.PGF = function(t){
		return Math.pow(1 - this.prob + this.prob * t, this.trials);
	};
	
	this.MGF = function(t){
		return this.PGF(Math.exp(t));
	};
	
	this.skew = function(){
		return (1 - 2 * this.prob) / Math.sqrt(this.trials * this.prob * (1 - this.prob));
	};
	
	this.kurt = function(){
		return (1 - 6 * this.prob * (1 - this.prob)) / (this.trials * this.prob * (1 - this.prob));
	};
	
	this.factorialMoment = function(n){
		return perm(this.trials, n) * Math.pow(this.prob, n);
	};
}
BinomialDistribution.prototype = new Distribution();

//The binomial distribution with the number of trials randomized
function BinomialNDistribution(dist, prob){
	this.dist = dist;
	this.prob = prob;
	this.minValue = 0;
	this.maxValue = this.dist.maxValue;
	this.step = 1;
	this.type = 0;
	this.data = new Data(this.minValue, this.maxValue, this.step);
	
	this.density = function(x){
		var sum = 0;
		for (var n = x; n <= this.maxValue; n = n + this.dist.step) sum = sum + this.dist.density(n) * binomial(n, x) * Math.pow(this.prob, x) * Math.pow(1 - this.prob, n - x);
		return sum;
	};
		
	this.mean = function(){
		return this.dist.mean() * this.prob;
	};
	
	this.variance = function(){
		return this.dist.mean() * this.prob * (1 - this.prob) + this.prob * this.prob * this.dist.variance();
	};
		
	this.simulate = function(){
		var trials = Math.round(this.dist.simulate());
		var successes = 0;
		for (var i = 0; i <= trials; i++) if (Math.random() <= this.prob) successes++;
		this.setValue(successes);
		return successes;
	};
}
BinomialNDistribution.prototype = new Distribution();

//Negative binomial distribution
function NegativeBinomialDistribution(successes, prob){
	this.prob = prob;
	this.successes = successes;
	var mean = this.successes / this.prob, variance = this.successes * (1 - this.prob) / (this.prob * this.prob);
	this.minValue = this.successes;
	this.maxValue = mean + 4 * Math.sqrt(variance);
	this.step = 1;
	this.data = new Data(this.minValue, this.maxValue, this.step);
	this.type = 0;
	
	this.mode = function(){
		return Math.floor((this.successes - 1) / this.prob + 1);
	};
		
	this.density = function(x){
		var n = Math.round(x);
		if (n < this.successes) return 0;
		else return binomial(n - 1, this.successes - 1) * Math.pow(this.prob, this.successes) * Math.pow(1 - this.prob, n - this.successes);
	};
		
	this.mean = function(){
		return mean;
	};
	
	this.variance = function(){
		return variance;
	};
		
	this.simulate = function(){
		var count = 0, trials = 0;
		while (count < this.successes){
			if (Math.random() < this.prob) count++;
			trials++;
		}
		this.setValue(trials);
		return trials;
	};
	
	this.PGF = function(t){
		if (Math.abs(t) >= 1 / (1 - this.prob)) return NaN;
		else return Math.pow(this.prob * t / (1 - (1 - this.prob) * t), this.successes);
	};
	
	this.MGF = function(t){
		return this.PGF(Math.exp(t));
	};
	
	this.skew = function(){
		return (2 - this.prob) / Math.sqrt(this.successes * (1 - this.prob));
	};
	
	this.kurt = function(){
		return (1 / this.successes) * (6 + this.prob * this.prob / (1 - this.prob));
	};
}
NegativeBinomialDistribution.prototype = new Distribution();

//Normal distribution
function NormalDistribution(mu, sigma){
	this.mu = mu;
	this.sigma = sigma;
	this.minValue = this.mu - 4 * this.sigma;
	this.maxValue = this.mu + 4 * this.sigma;
	this.step = (this.maxValue - this.minValue) / 100;
	this.data = new Data(this.minValue, this.maxValue, this.step);
	this.type = 1;
	var c = 1 / (this.sigma * Math.sqrt(2 * Math.PI));
	
	this.mode = function(){
		return this.mu;
	};
	
	this.maxDensity = function(){
		return c;
	};
	
	this.density = function(x){
		var z = (x - this.mu) / this.sigma;
		return c * Math.exp(-z * z / 2);
	};
	
	this.CDF = function(x){
		var z = (x - this.mu) / this.sigma;
		if (z >= 0) return 0.5 + 0.5 * gammaCDF(0.5 * z * z, 0.5);
		else return 0.5 - 0.5 * gammaCDF(0.5 * z * z, 0.5);
	};	
	
	this.simulate = function(){
		var r = Math.sqrt(-2 * Math.log(Math.random()));
		var theta = 2 * Math.PI * Math.random();
		var x = this.mu + this.sigma * r * Math.cos(theta);
		this.setValue(x); 
		return x;
	};
		
	this.mean = function(){
		return this.mu;
	};
	
	this.variance = function(){
		return this.sigma * this.sigma;
	};
	
	this.stdDev = function(){
		return this.sigma;
	};
	
	this.MGF = function(t){
		var s = this.sigma * t;
		return Math.exp(this.mu + 0.5 * s * s);
	};
	
	this.PGF = function(t){
		if (t <= 0) return NaN;
		else return this.MGF(Math.log(t));
	};
	
	this.centralMoment = function(n){
		if (isOdd(n)) return 0;
		else if (isEven(n)){
			var k = n / 2;
			return (factorial(n) / (factorial(k) * Math.pow(2, k))) * Math.pow(this.sigma, n);
		}
	};
}
NormalDistribution.prototype = new Distribution();

//Gamma Distribution
function GammaDistribution(shape, scale){
	this.shape = shape;
	this.scale = scale;
	if (this.shape >= 1) this.minValue = 0; else this.minValue = 0.01;
	var mean = shape * scale;
	var variance = this.shape * this.scale * this.scale;
	var stdDev = Math.sqrt(this.shape) * this.scale;
	var c = 1 / (gamma(this.shape) * Math.pow(this.scale, this.shape));
	this.maxValue = mean + 4 * stdDev;
	this.step = (this.maxValue - this.minValue) / 100;
	this.type = 1;
	this.data = new Data(this.minValue, this.maxValue, this.step);
	
	this.mode = function(){
		if (this.shape < 1) return this.minValue;
		else return this.scale * (this.shape - 1);
	};
	
	this.density = function(x){
		return c * Math.pow(x, this.shape - 1) * Math.exp(-x / this.scale);
	};
	
	this.CDF = function(x){
		return gammaCDF(x / this.scale, this.shape);
	};
		
	this.mean = function(){
		return mean;
	};
	
	this.variance = function(){
		return variance;
	};
	
	this.stdDev = function(){
		return stdDev;
	};
}
GammaDistribution.prototype = new Distribution();	

//Chi-square distribution
function ChiSquareDistribution(df){
	this.df = df;
	if (this.df == 1) this.minValue = 0.1;
	else this.minValue = 0;
	var c = 1 / (Math.pow(2, this.df / 2) * gamma(this.df / 2));
	this.maxValue = this.df + 4 * Math.sqrt(2 * this.df);
	this.step = (this.maxValue - this.minValue) / 100;
	this.type = 1;
	this.data = new Data(this.minValue, this.maxValue, this.step);
	
	this.mode = function(){
		if (this.df < 2) return this.minValue;
		else return this.df - 2;
	};
	
	this.density = function(x){
		return c * Math.pow(x, this.df / 2 - 1) * Math.exp(-x / 2);
	};

	this.CDF = function(x){
		return gammaCDF(x / 2, this.df / 2);
	};
			
	this.mean = function(){
		return this.df;
	};
	
	this.variance = function(){
		return 2 * this.df;
	};
			
	this.simulate = function(){
		var V, Z, r, theta;
		V = 0;
		for (var i = 1; i <= this.df; i++){
			r = Math.sqrt(-2 * Math.log(Math.random()));
			theta = 2 * Math.PI * Math.random();
			Z = r * Math.cos(theta);
			V = V + Z * Z;
		}
		this.setValue(V);
		return V;
	};
}
ChiSquareDistribution.prototype = new Distribution();

//Student t-distribution
function StudentDistribution(df){
	this.df = df;
	var c = gamma((this.df + 1) / 2) / (Math.sqrt(this.df * Math.PI) * gamma(this.df / 2));
	if (this.df == 1){
		this.maxValue = 8;
		this.minValue = -8;
	}
	else if (this.df == 2){
		this.maxValue = 7;
		this.minValue = -7;
	}
	else{
		this.maxValue = 4 * Math.sqrt(this.df / (this.df - 2));
		this.minValue = -this.maxValue;
	}
	this.step = (this.maxValue - this.minValue) / 100;
	this.type = 1;
	this.data = new Data(this.minValue, this.maxValue, this.step);
	
	this.mode = function(){
		return 0;
	};
		
	this.density = function(x){
		return c * Math.pow(1 + x * x / this.df, -(this.df + 1) / 2);
	};

	this.CDF = function(x){
		var u = this.df / (this.df + x * x);
		if (x > 0) return 1 - 0.5 * betaCDF(u, 0.5 * this.df, 0.5);
		else return 0.5 * betaCDF(u, 0.5 * this.df, 0.5);
	};
		
	this.mean = function(){
		if (this.df == 1) return NaN;
		else return 0;
	};
	
	this.variance = function(){
		if (this.df == 1) return NaN;
		else if (this.df == 2) return Infinity;
		else return this.df / (this.df - 2);
	};
			
	this.simulate = function(){
		var x, v, z, r, theta;
		v = 0;
		for (var i = 1; i <= this.df; i++){
			r = Math.sqrt(-2 * Math.log(Math.random()));
			theta = 2 * Math.PI * Math.random();
			z = r * Math.cos(theta);
			v = v + z * z;
		}
		r = Math.sqrt(-2 * Math.log(Math.random()));
		theta = 2 * Math.PI * Math.random();
		z = r * Math.cos(theta);
		x = z / Math.sqrt(v / this.df);
		this.setValue(x);
		return x;
	};
}
StudentDistribution.prototype = new Distribution();

//F distribution
function FDistribution(num, den){
	this.num = num; 
	this.den = den;
	var c = (gamma((this.num + this.den) / 2) / (gamma(this.num / 2) * gamma(this.den / 2))) * Math.pow(this.num / this.den, this.num / 2);
	if (this.den == 1) this.minValue = 0.1; 
	else this.minValue = 0;
	if (this.den <= 4) this.maxValue = 20; 
	else this.maxValue = this.den / (this.den - 2)  + 4 * Math.sqrt(2.0 * (this.den / (this.den - 2)) * (this.den / (this.den - 2)) * (this.den + this.num - 2) / (this.num * (this.den - 4)));
	this.step = (this.maxValue - this.minValue) / 100;
	this.type = 1;
	this.data = new Data(this.minValue, this.maxValue, this.step);
	
	this.mode = function(){
		if (this.num <= 2) return this.minValue;
		else return ((this.num - 2) * this.den) / (this.num * (this.den + 2));
	};
	
	this.density = function(x){
		return c * Math.pow(x, (this.num - 2) / 2) / Math.pow(1 + (this.num / this.den) * x, (this.num + this.den) / 2);

	};

	this.CDF = function(x){
		var u = this.den / (this.den + this.num * x);
		if (x < 0) return 0;
		else return 1 - betaCDF(u, 0.5 * this.den, 0.5 * this.num);
	};
		
	this.mean = function(){
		if (this.den <= 2) return Infinity;
		else return this.den / (this.den - 2);
	};
	
	this.variance = function(){
		if (this.den <= 2) return NaN;
		else if (this.den <= 4) return Infinity;
		else return 2.0 * (this.den / (this.den - 2)) * (this.den / (this.den - 2))	* (this.den + this.num - 2) / (this.num * (this.den - 4));
	};
		
	this.simulate = function(){
		var x, U, V, Z, r, theta;
		U = 0;
		for (var i = 1; i <= this.num; i++){
			r = Math.sqrt(-2 * Math.log(Math.random()));
			theta = 2 * Math.PI * Math.random();
			Z = r * Math.cos(theta);
			U = U + Z * Z;
		}
		V = 0;
		for (var j = 1; j <= this.den; j++){
			r = Math.sqrt(-2 * Math.log(Math.random()));
			theta = 2 * Math.PI * Math.random();
			Z = r * Math.cos(theta);
			V = V + Z * Z;
		}
		x = (U / this.num) / (V / this.den);
		this.setValue(x);
		return x;
	};	
}
FDistribution.prototype = new Distribution();

//Beta distribution
function BetaDistribution(left, right){
	this.left = left;
	this.right = right;
	var c = gamma(this.left + this.right) / (gamma(this.left) * gamma(this.right));
	if (this.left < 1) this.minValue = 0.01; else this.minValue = 0;
	if (this.right < 1) this.maxValue = 0.99; else this.maxValue = 1;
	this.step = 0.01;
	this.type = 1;
	this.data = new Data(this.minValue, this.maxValue, this.step);
	
	this.mode = function(){
		var m;
		if (this.left < 1 && this.right < 1){
			if (this.left < this.right) m = 0.01; else m = 0.99;
		}
		else if (this.left < 1 && this.right >= 1) m = 0.01;
		else if (this.left >= 1 && this.right < 1) m = 0.99;
		else m = (this.left - 1) / (this.left + this.right - 2);
		return m;
	};
	
	this.density = function(x){
		return c * Math.pow(x, this.left - 1) * Math.pow(1 - x, this.right - 1);
	};

	this.CDF = function(x){
		return betaCDF(x, this.left, this.right);
	};
			
	this.mean = function(){
		return this.left / (this.left + this.right);
	};
	
	this.variance = function(){
		return this.left * this.right / ((this.left + this.right) * (this.left + this.right) * (this.left + this.right + 1));
	};
}
BetaDistribution.prototype = new Distribution();

//Weibull distribution
function WeibullDistribution(shape, scale){
	this.shape = shape;
	this.scale = scale;
	var c = this.shape / Math.pow(this.scale, this.shape);
	var mean = this.scale * gamma(1 + 1 / this.shape);
	var variance = this.scale * this.scale * gamma(1 + 2 / this.shape) - mean * mean;
	this.minValue = 0;
	this.maxValue = mean + 4 * Math.sqrt(variance);
	this.step = this.maxValue / 100;
	this.type = 1;
	this.data = new Data(this.minValue, this.maxValue, this.step);

	this.density = function(x){
		return c * Math.pow(x, this.shape - 1) * Math.exp(-Math.pow(x / this.scale, this.shape));
	};
	
	this.mode = function(){
		if (this.shape < 1) return this.minValue;
		else return this.scale * Math.pow((this.shape - 1) / this.shape, 1 / this.shape);
	};	
	
	this.CDF = function(x){
		return 1 - Math.exp(-Math.pow(x / this.scale, this.shape));
	};
	
	this.quantile = function(p){
		return this.scale * Math.pow(-Math.log(1 - p), 1 / this.shape);
	};
		
	this.mean = function(){
		return mean;
	};
	
	this.variance = function(){
		return variance;
	};
}
WeibullDistribution.prototype = new Distribution();

//Pareto distribution
function ParetoDistribution(shape, scale){
	this.shape = shape;
	this.scale = scale;
	var c = this.shape * Math.pow(this.scale, this.shape);
	this.minValue = this.scale;
	this.maxValue = this.scale * (1 + 6 / this.shape);
	this.step = (this.maxValue - this.minValue) / 100;
	this.type = 1;
	this.data = new Data(this.minValue, this.maxValue, this.step);

	this.density = function(x){
		if (x < this.scale) return 0;
		else return c / Math.pow(x, this.shape + 1);
	};
	
	this.mode = function(){
		return this.scale;
	};
	
	this.CDF = function(x){
		return 1 - Math.pow(this.scale / x, this.shape);
	};
	
	this.quantile = function(p){
		return this.scale / Math.pow((1 - p), 1 / this.shape);
	};
		
	this.mean = function(){
		if (this.shape <= 1) return Infinity;
		else return (this.shape * this.scale) / (this.shape - 1);
	};
	
	this.variance = function(){
		if (this.shape <= 1) return NaN;
		else if (this.shape > 1 && this.shape <= 2) return Infinity;
		else return (this.shape * this.scale * this.scale) / ((this.shape - 1) * (this.shape - 2) * (this.shape - 2));
	};
}
ParetoDistribution.prototype = new Distribution();

//Logistic distribution
function LogisticDistribution(location, scale){
	this.location = location;
	this.scale = scale;
	var mean = this.location, variance = (this.scale * this.scale * Math.PI * Math.PI) / 3, stdDev = Math.sqrt(variance);
	this.minValue = mean - 4 * stdDev; 
	this.maxValue = mean + 4 * stdDev;
	this.step = (this.maxValue - this.minValue) / 100;
	this.type = 1;
	this.data = new Data(this.minValue, this.maxValue, this.step);
	
	this.density = function(x){
		var e = Math.exp((x - this.location) / this.scale);
		return e / (this.scale * (1 + e) * (1 + e));
	};
	
	this.mode = function(){
		return this.location;
	};
	
	this.CDF = function(x){
		var e = Math.exp((x - this.location) / this.scale);
		return e / (1 + e);
	};
	
	this.quantile = function(p){
		return this.location + this.scale * Math.log(p / (1 - p));
	};
	
	this.mean = function(){
		return mean;
	};
	
	this.variance = function(){
		return variance;
	};
}
LogisticDistribution.prototype = new Distribution();

//Lognormal distribution
function LogNormalDistribution(mu, sigma){
	this.mu = mu;
	this.sigma = sigma;
	var mean = Math.exp(this.mu + this.sigma * this.sigma / 2);
	var variance = Math.exp(2 * (this.mu + this.sigma * this.sigma)) - Math.exp(2 * this.mu + this.sigma * this.sigma);
	this.minValue = 0;
	this.maxValue = mean + 4 * Math.sqrt(variance);
	this.step = (this.maxValue - this.minValue) / 100;
	this.type = 1;
	this.data = new Data(this.minValue, this.maxValue, this.step);
	
	this.density = function(x){
		if (x === 0) return 0; 
		else return Math.exp(-(Math.log(x) - this.mu) * (Math.log(x) - this.mu) / (2 * this.sigma * this.sigma)) / (Math.sqrt(2 * Math.PI) * this.sigma * x);
	};
	
	this.mode = function(){
		return Math.exp(this.mu - this.sigma * this.sigma);
	};
	
	this.CDF = function(x){
		var z = (Math.log(x) - this.mu) / this.sigma;
		if (z >= 0) return 0.5 + 0.5 * gammaCDF(0.5 * z * z, 0.5);
		else return 0.5 - 0.5 * gammaCDF(0.5 * z * z, 0.5);
	};
	
	this.mean = function(){
		return mean;
	};
	
	this.variance = function(){
		return variance;
	};
		
	this.simulate = function(){
		var r = Math.sqrt(-2 * Math.log(Math.random()));
		var theta = 2 * Math.PI * Math.random();
		var x = Math.exp(this.mu + this.sigma * r * Math.cos(theta));
		this.setValue(x); 
		return x;
	};	
}
LogNormalDistribution.prototype = new Distribution();

//Extreme value distribution	
function ExtremeValueDistribution(location, scale){
	this.location = location;
	this.scale = scale;
	var g = 0.5772156649;
	var mean = this.location + this.scale * g;
	var variance = (this.scale * this.scale * Math.PI * Math.PI) / 6;
	var stdDev = Math.sqrt(variance);
	this.minValue = mean - 4 * stdDev;
	this.maxValue = mean + 4 * stdDev;
	this.step = (this.maxValue - this.minValue) / 100;
	this.type = 1;
	this.data = new Data(this.minValue, this.maxValue, this.step);
	
	this.density = function(x){
		var e = Math.exp(-(x - this.location) / this.scale);
		return e * Math.exp(-e) / this.scale;
	};
	
	this.mode = function(){
		return this.location;
	};
	
	this.CDF = function(x){
		return Math.exp(-Math.exp(-(x - this.location) / this.scale));
	};
	
	this.quantile = function(p){
		return this.location - this.scale * Math.log(-Math.log(p));
	};
	
	this.mean = function(){
		return mean;
	};
	
	this.variance = function(){
		return variance;
	};
}
ExtremeValueDistribution.prototype = new Distribution();

//Poisson distribution
function PoissonDistribution(rate){
	this.rate = rate;
	this.minValue = 0;
	this.maxValue = this.rate + 4 * Math.sqrt(this.rate);
	this.step = 1;
	this.type = 0;
	this.data = new Data(this.minValue, this.maxValue, this.step);
	
	this.density = function(x){
		return Math.exp(-this.rate) * Math.pow(this.rate, x) / factorial(x);
	};
	
	this.mode = function(){
		return Math.floor(this.rate);
	};
	
	this.CDF = function(x){
		return 1 - gammaCDF(this.rate, x + 1);
	};
		
	this.mean = function(){
		return this.rate;
	};
	
	this.variance = function(){
		return this.rate;
	};
			
	this.simulate = function(){
		var arrivals = 0;
		var sum = -Math.log(1 - Math.random());
		while (sum <= this.rate){
			arrivals++;
			sum = sum - Math.log(1 - Math.random());
		}
		this.setValue(arrivals);
		return arrivals;
	};	
}
PoissonDistribution.prototype = new Distribution();

//Uniform distribution
function UniformDistribution(left, right){
	this.left = left;
	this.right = right;
	this.minValue = this.left;
	this.maxValue = this.right;
	this.step = (this.right - this.left) / 100;
	this.type = 1;
	this.data = new Data(this.minValue, this.maxValue, this.step);
	
	this.density = function(x){
		return 1 / (this.right - this.left);
	};
	
	this.mode = function(){
		return this.left;
	};
		
	this.CDF = function(x){
		if (x < this.left) return 0;
		else if (x > this.right) return 1;
		else return (x - this.left) / (this.right - this.left);
	};
	
	this.quantile = function(p){
		if (p < 0 || p > 1) return NaN;
		else return this.left + p * (this.right - this.left);
	};
		
	this.mean = function(){
		return (this.left + this.right) / 2;
	};
	
	this.variance = function(){
		return (Math.pow(this.right - this.left, 2)) / 12;
	};
}	
UniformDistribution.prototype = new Distribution();

//The hypergeometric distribution
function HypergeometricDistribution(population, red, sample){
	this.population = population;
	this.red = red;
	this.sample = sample;
	this.type = 0;
	this.minValue = Math.max(0, this.sample - (this.population - this.red));
	this.maxValue = Math.min(this.sample, this.red);
	this.step = 1;
	this.data = new Data(this.minValue, this.maxValue, this.step);
	
	this.density = function(x){
		var k = Math.round(x);
		return binomial(this.red, k) * binomial(this.population - this.red, this.sample - k) / binomial(this.population, this.sample);
	};
	
	this.mode = function(){
		return Math.floor((this.red + 1) * (this.sample + 1) / (this.population + 2));
	};
	
	this.mean = function(){
		return this.sample * (this.red / this.population);
	};
	
	this.variance = function(){
		return this.sample * (this.red / this.population) * (1 - this.red / this.population) * (this.population - this.sample) / (this.population - 1);
	};		
}
HypergeometricDistribution.prototype = new Distribution();

//Polya distribution
function PolyaDistribution(red, green, add, trials){
	this.red = red;
	this.green = green;
	this.add = add;
	this.trials = trials;
	this.type = 0;
	this.minValue = 0;
	this.maxValue = this.trials;
	this.step = 1;
	this.data = new Data(this.minValue, this.maxValue, this.step);
	
	//Methods
	this.density = function(x){
		var m = this.red + this.green;
		var prod1 = 1, prod2 = 1;
		for (var i = 0; i < x; i++) prod1 = prod1 * (this.trials - i) * (this.red + i * this.add) / ((m + i * this.add) * (x - i));
		for (var j = 0; j < n - x; j++) prod2 = prod2 * (b + j * c) / (m + (x + j) * c);
		return prod1 * prod2;
	};
	
	this.mean = function(){
		return this.trials * this.red / (this.red + this.green);
	};
	
	this.variance = function(){
		var p = this.red / (this.red + this.green);
		var r = this.add / (this.red + this.green + this.add);
		return this.trials * p * (1 - p) * (1 + (this.trials - 1) * r);
	};
	
	this.simulate = function(){
		var successes = 0, totalRed = this.red, totalGreen = this.green;
		var p;
		for (var i = 1; i <= n; i++){
			p = totalRed / (totalRed + totalGreen);
			if (Math.random() < p) {
				successes++;
				totalRed = totalRed + this.add;
			}
			else totalGreen = totalGreen + this.add;
		}
		this.setValue(successes);
		return successes;
	};
		
}
PolyaDistribution.prototype = new Distribution();

//Birthday Distribution
function BirthdayDistribution(days, sample){
	this.days = days;
	this.sample = sample;
	this.minValue = 1;
	this.maxValue = Math.min(this.days, this.sample);
	this.step = 1;
	this.type = 0;
	this.data = new Data(this.minValue, this.maxValue, this.step);

	var upperIndex;
	var prob = new Array(this.sample + 1);
	for (var i = 0; i < this.sample + 1; i++){
		prob[i] = new Array(this.days + 1);
		for (var j = 0; j < this.days + 1; j++) prob[i][j] = 0;
	}
	prob[0][0] = 1; prob[1][1] = 1;
	for (var k = 1; k < this.sample; k++){
		if (k < this.days) upperIndex = k + 1; else upperIndex = this.days;
		for (var l = 1; l <= upperIndex; l++){
			prob[k+1][l] = prob[k][l] * (l / this.days) + prob[k][l - 1] * ((this.days - l + 1) / this.days);
		}
	}

	var mode = 1;
	for (var x = 1; x <= Math.min(this.days, this.sample); x++) if (prob[this.sample][x] > prob[this.sample][mode]) mode = x;
	

	this.density = function(x){
		return prob[this.sample][x];
	};

	this.mode = function(){
		return mode;
	};

	this.mean = function(){
		return this.days * (1 - Math.pow(1 - 1 / this.days, this.sample));
	};

	this.variance = function(){
		return this.days * (this.days - 1) * Math.pow(1 - 2 / this.days, this.sample) + this.days * Math.pow(1 - 1 / this.days, this.sample) - this.days * this.days * Math.pow(1 - 1 / this.days, 2 * this.sample);
	};

	this.simulate = function(){
		var count = new Array(this.days);
		var distinct = 0;
		for (var i = 1; i <= this.sample; i++){
			var j = Math.floor(this.days * Math.random());
			if (count[j] === 0) distinct++;
			count[j] = count[j]++;
		}
		this.setValue(distinct);
		return distinct;
	};
}
BirthdayDistribution.prototype = new Distribution();

//Finite order statistic distribution
function FiniteOrderStatistic(population, sample, order){
	this.population = population;
	this.sample = sample;
	this.order = order;
	this.type = 0;
	this.minValue = this.order;
	this.maxValue = this.population - this.sample + this.order;
	this.step = 1;
	this.data = new Data(this.minValue, this.maxValue, this.step);
	
	this.density = function(x){
		return binomial(x - 1, this.order - 1) * binomial(this.population - x, this.sample - this.order) / binomial(this.population, this.sample);
	};
			
	this.mean = function(){
		return this.order * (this.population + 1) / (this.sample + 1);
	};
	
	this.variance = function(){
		return (this.population + 1) * (this.population - this.sample) * this.order * (this.sample + 1 - this.order) / ((this.sample + 1) * (this.sample + 1) * (this.sample + 2));
	};		
}
FiniteOrderStatistic.prototype = new Distribution();

//Matching distribtion
function MatchDistribution(hats){
	this.hats = hats;
	this.minValue = 0;
	this.maxValue = this.hats;
	this.step = 1;
	this.data = new Data(this.minValue, this.maxValue, this.step);
	this.type = 0;

	this.density = function(x){
		var sum = 0, sign = -1;
		for (var j = 0; j <= this.hats - x; j++){
			sign = -sign;
			sum = sum + sign / factorial(j);
		}
		return sum / factorial(x);	
	};
	
	this.mode = function(){
		if (this.hats == 2) return 0; 
		else return 1;
	};
	
	this.mean = function(){
		return 1;
	};
	
	this.variance = function(){
		return 1;
	};
	
	this.simulate = function(){
		var p = new Array(this.hats), s = new Array(hats), sum = 0;
		for (var i = 0; i < hats; i++) p[i] = i + 1;
		s = sample(p, hats);
		for (var j = 0; j < hats; j++) if (s[j] == j + 1) sum++;
		this.setValue(sum);
		return sum;
	};
}
MatchDistribution.prototype = new Distribution();

//Discrete arcsine distribution
function DiscreteArcsineDistribution(trials){
	this.trials = trials;
	this.minValue = 0;
	this.maxValue = this.trials;
	this.step = 2;
	this.type = 0;
	this.data = new Data(this.minValue, this.maxValue, this.step);
	
	this.density = function(x){
		if (isEven(x) && x >= 0 && x <= n) return binomial(x, x / 2) * binomial(this.trials - x, (this.trials - x) / 2) / Math.pow(2, this.trials);
		else return 0;
	};
	
	this.mode = function(){
		return 0;
	};
	
	this.mean = function(){
		return this.trials / 2;
	};
	
	this.simulate = function(){
		var step, lastZero = 0, position = 0;
		for (var i = 1; i <= this.trials; i++){
			if (Math.random() < 0.5) step = 1;
			else step = -1;
			position = position + step;
			if (position === 0) lastZero = i;
		}
		this.setValue(lastZero);
		return lastZero;
	};
}
DiscreteArcsineDistribution.prototype = new Distribution();

//Triangle distribution
function TriangleDistribution(left, right, middle){
	this.left = left;
	this.right = right;
	this.middle = middle; 
	this.minValue = this.left;
	this.maxValue = this.right;
	this.step = (this.right - this.left) / 100;
	this.type = 1;
	this.data = new Data(this.minValue, this.maxValue, this.step);
	
	this.density = function(x){
		var y;
		if (this.left <= x && x <= this.middle) y =  2 * (x - this.left) / ((this.right - this.left) * (this.middle - this.left));
		else if (this.middle < x && x <= this.right) y = 2 * (this.right - x) / ((this.right - this.left) * (this.right - this.middle));
		else y = 0;
		if (isNaN(y)) y = 2 / (this.right - this.left);
		return y;
	};
	
	this.mode = function(){
		return this.middle;
	};
	
	this.maxDensity = function(){
		return 2 / (this.right - this.left);
	};
	
	this.mean = function(){
		return (this.left + this.right + this.middle) / 3;
	};
	
	this.variance = function(){
		return (this.left * this.left + this.right * this.right + this.middle * this.middle - this.left * this.right - this.left * this.middle - this.right * this.middle) / 18;
	};
	
	this.quantile = function(p){
		if (0 <= p && p <= (this.middle - this.left) / (this.right - this.left)) return this.left + Math.sqrt((this.right - this.left) * (this.middle - this.left) * p);
		else return this.right - Math.sqrt((1 - p) * (this.right - this.left) * (this.right - this.middle));
	};
}
TriangleDistribution.prototype = new Distribution();

//Semicircle distribution
function SemiCircleDistribution(r){
	this.minValue = -r;
	this.maxValue = r;
	this.step = r / 50;
	this.type = 1;
	this.data = new Data(this.minValue, this.maxValue, this.step);
	
	this.density = function(x){
		if (x <= -r || x >= r) return 0; 
		else return 2 * Math.sqrt(r * r - x * x) / (Math.PI * r * r);
	};
	
	this.CDF = function(x){
		return 0.5 + (Math.asin(x / r) + (x / r) * Math.sqrt(1 - x * x / (r * r)))/Math.PI;
	};
	
	this.mode = function(){
		return 0;
	};
	
	this.maxDensity = function(){
		return 2 / (Math.PI * r);
	};
	
	this.mean = function(){
		return 0;
	};
	
	this.variance = function(){
		return r * r / 4;
	};

}
SemiCircleDistribution.prototype = new Distribution();

//Coupon collector distribution
function CouponDistribution(m, k){
	this.minValue = k;
	var mu = 0, s2 = 0, j;
	for (var i = 1; i <= k; i++) mu = mu + m / (m - i + 1);
	for (var i1 = 1; i1 <= k; i1++) s2 = s2 + (m * (i1 - 1)) / ((m - i1 + 1) * (m - i1 + 1));
	this.maxValue = Math.round(mu + 4 * Math.sqrt(s2));
	this.step = 1;
	this.type = 0;
	this.data = new Data(this.minValue, this.maxValue, this.step);
	var prob = new Array(this.maxValue + 1);
	for (var i2 = 0; i2 <  this.maxValue + 1; i2++) prob[i2] = new Array(m + 1);
	for (var i3 = 0; i3 < this.maxValue + 1; i3++) for (var j1 = 0; j1 < m + 1; j1++) prob[i3][j1] = 0;
	prob[0][0] = 1; prob[1][1] = 1;
	for (var i4 = 1; i4 < this.maxValue; i4++){
		if (i4 < m) j = i4 + 1; else j = m;
		for (var n = 1; n <= j; n++) prob[i4 + 1][n] = prob[i4][n] * (n / m) + prob[i4][n - 1] * ((m - n + 1) / m);
	}
	
	this.density = function(x){
		return ((m - k + 1) / m) * prob[x - 1][k - 1];
	};

	this.mean = function(){
		return mu;
	};

	this.variance = function(){
		return s2;
	};

	this.simulate = function(){
		var cellCount = new Array(m);
		var occupiedCells = 0;
		var ballCount = 0;
		while (occupiedCells <= k){
			ballCount++;
			var ballIndex = Math.floor(m * Math.random());
			if (cellCount[ballIndex] === 0) occupiedCells++;
			cellCount[ballIndex] = cellCount[ballIndex]++;
		}
		this.setValue(ballCount);
		return ballCount;
	};
}
CouponDistribution.prototype = new Distribution();

//Distribution of the maximum position in the simple random walk
function WalkMaxDistribution(n){
	this.minValue = 0;
	this.maxValue = n;
	this.step = 1;
	this.type = 0;
	this.data = new Data(0, n, 1);
	
	this.density = function(x){
		var m;
		if ((x + n) % 2 === 0) m = (x + n) / 2;
		else m = (x + n + 1) / 2;
		return binomial(n, m) / Math.pow(2 , n);
	};
	
	this.maxDensity = function(){
		return this.density(0);
	};
			
	this.simulate = function(){
		var step, max = 0, position = 0;
		for (var i = 1; i <= n; i++){
			if (Math.random() < 0.5) step = 1;
			else step = -1;
			position = position + step;
			if (position > max) max = position;
		}
		this.setValue(max);
		return max;
	};	
}
WalkMaxDistribution.prototype = new Distribution();

//Distribution of the final position in the simple random walk
function WalkPositionDistribution(n){
	this.minValue = -n;
	this.maxValue = n;
	this.step = 2;
	this.type = 0;
	this.data = new Data(-n, n, 2);
	
	this.density = function(x){
		return binomial(n, (x + n) / 2) / Math.pow(2, n);
	};
	
	this.maxDensity = function(){
		return this.density(0);
	};
		
	this.mean = function(){
		return 0;
	};
	
	this.variance = function(){
		return n;
	};
		
	this.simulate = function(){
		var step, position = 0;
		for (var i = 1; i <= n; i++){
			if (Math.random() < 0.5) step = 1;
			else step = -1;
			position = position + step;
		}
		this.setValue(position);
		return position;
	};
}
WalkPositionDistribution.prototype = new Distribution();

//The location scale distribution associated with a given distribution, location parameter a, scale parameter b
function LocationScaleDistribution(dist, location, scale){
	this.dist = dist;
	this.location = location;
	this.scale = scale;
	this.minValue = this.location + this.scale * this.dist.minValue;
	this.maxValue = this.location + this.scale * this.dist.maxValue;
	this.step = this.scale * this.dist.step;
	this.data = new Data(this.minValue, this.maxValue, this.step);
	this.type = this.dist.type;
	
	this.density = function(x){
		var y = this.dist.minValue + Math.round((x - this.minValue) / this.step) * this.dist.step;
		if (this.type === 0) return this.dist.density(y);
		else return this.dist.density(y) / this.scale;
	};
	
	this.mode = function(){
		return this.location + this.scale * this.dist.mode;
	};
	
	this.maxDensity = function(){
		if (this.type === 0) return this.dist.maxDensity();
		else return this.dist.maxDensity() / this.scale;
	};
		
	this.mean = function(){
		return this.location + this.scale * this.dist.mean();
	};
	
	this.variance = function(){
		return this.scale * this.scale * this.dist.variance();
	};
		
	this.simulate = function(){
		var x = this.location + this.scale * this.dist.simulate();
		this.setValue(x);
		return x;
	};	
}
LocationScaleDistribution.prototype = new Distribution();

//Convolution power of a distribution
function Convolution(d, n){
	this.dist = d;
	this.power = n;
	this.minValue = this.power * this.dist.minValue;
	this.maxValue = this.power * this.dist.maxValue;
	this.step = this.dist.step;
	this.data = new Data(this.minValue, this.maxValue, this.step);
	this.type = this.dist.type;
	
	var a = this.dist.minValue, b = this.dist.maxValue, s = this.dist.step;
	var m = Math.round((b - a) / s) + 1;
	var delta = 1;
	if (this.type == 1) delta = this.step;
	var pdf = new Array(this.power);
	for (var k = 0; k < n; k++) pdf[k] = new Array((k + 1) * m - k);
	for (var j = 0; j < m; j++) pdf[0][j] = this.dist.density(a + j * s);
	for (var k1 = 1; k1 < n; k1++){
		for (var j1 = 0; j1 < (k1 + 1) * m - k1; j1++){
			var sum = 0;
			for (var i = Math.max(0, j1 - m + 1); i < Math.min(j1 + 1, k1 * m - k1 + 1); i++)	sum = sum + pdf[k1 - 1][i] * pdf[0][j1 - i] * delta;
			pdf[k1][j1] = sum;
		}
	}
	
	this.density = function(x){
		var index = Math.round((x - this.minValue) / this.step);
		return pdf[this.power - 1][index];
	};
			
	this.mean = function(){
		return this.power * this.dist.mean();
	};
	
	this.variance = function(){
		return this.power * this.dist.variance();
	};
		
	this.simulate = function(){
		var sum = 0;
		for (i = 1; i <= this.power; i++) sum = sum + this.dist.simulate();
		this.setValue(sum);
		return sum;
	};
}
Convolution.prototype = new Distribution();

//Distribution of an order statistic from a given distribution
function OrderStatistic(dist, sample, order){
	this.dist = dist;
	this.sample = sample;
	this.order = order;	
	this.type = this.dist.type;
	this.minValue = this.dist.minValue;
	this.maxValue = this.dist.maxValue;
	this.step = this.dist.step;
	this.data = new Data(this.minValue, this.maxValue, this.step);
	
	this.density =  function(x){
		if (this.type === 0) return this.CDF(x) - this.CDF(x - this.step);
		else {
			var p = this.dist.CDF(x);
			return this.order * binomial(this.sample, this.order) * Math.pow(p, this.order - 1) * Math.pow(1 - p, this.sample - this.order) * this.dist.density(x);
		}
	};
		
	this.CDF = function(x){
		var sum = 0, p = this.dist.CDF(x);
		for (var j = this.order; j <= this.sample; j++) sum = sum + binomial(this.sample, j) * Math.pow(p, j) * Math.pow(1 - p, this.sample - j);
		return sum;
	};
	
	this.simulate = function(){
		var sampleValues = new Array(this.sample);
		var orderStats = new Array(this.sample);
		for (var i = 0; i < this.sample; i++) sampleValues[i] = this.dist.simulate();
		orderStats = sampleValues.sort(ascend);
		var x = orderStats[order - 1];
		this.setValue(x);
		return x;
	};
}
OrderStatistic.prototype = new Distribution();

//Cauchy distribution
function CauchyDistribution(scale){
	this.scale = scale;
	this.minValue = -5 * this.scale;
	this.maxValue = 5 * this.scale;
	this.step = (this.maxValue - this.minValue) / 100;
	this.data = new Data(this.minValue, this.maxValue, this.step);
	this.type = 1;
	
	this.mode = function(){
		return 0;
	};
	
	this.density = function(x){
		return this.scale / (Math.PI * ( this.scale * this.scale + x * x));
	};
	
	this.CDF = function(x){
		return 0.5 + (1 / Math.PI) * Math.atan(x / this.scale);
	};
	
	this.quantile = function(p){
		return this.scale * Math.tan(Math.PI * (p - 0.5));
	};	
	
	this.mean = function(){
		return NaN;
	};
	
	this.variance = function(){
		return NaN;
	};
	
}
CauchyDistribution.prototype = new Distribution();

//Arcsine distribution
function ArcsineDistribution(){
	this.minValue = 0.01;
	this.maxValue = 0.99;
	this.step = (this.maxValue - this.minValue) / 100;
	this.data = new Data(this.minValue, this.maxValue, this.step);
	this.type = 1;
	
	this.mode = function(){
		return NaN;
	};
	
	this.maxDensity = function(){
		return this.density(this.minValue);
	};
	
	this.density = function(x){
		return 1 / (Math.PI * Math.sqrt(x * (1 - x)));
	};
	
	this.CDF = function(x){
		return (2 / Math.PI) * Math.asin(Math.sqrt(x));
	};
	
	this.quantile = function(p){
		return Math.pow(Math.sin(p * Math.PI / 2), 2);
	};	
	
	this.mean = function(){
		return 1 / 2;
	};
	
	this.variance = function(){
		return 1 / 8;
	};
	
}
ArcsineDistribution.prototype = new Distribution();

//Hyperbolic Secant distribution
function HyperbolicSecantDistribution(location, scale){
	this.location = location;
	this.scale = scale;
	this.minValue = this.location - 5 * this.scale;
	this.maxValue = this.location + 5 * this.scale;
	this.step = (this.maxValue - this.minValue) / 100;
	this.data = new Data(this.minValue, this.maxValue, this.step);
	this.type = 1;
	
	this.mode = function(){
		return this.location;
	};
	
	this.density = function(x){
		var t = (Math.PI / 2) * ((x - this.location) / this.scale);
		return 1 / (this.scale * (Math.exp(t) + Math.exp(-t)));
	};
	
	this.CDF = function(x){
		return (2 / Math.PI) * Math.atan(Math.exp((Math.PI/2) * (x - this.location) / this.scale));
	};
	
	this.quantile = function(p){
		return this.location + this.scale * (2 / Math.PI) * Math.log(Math.tan((Math.PI / 2) * p));
	};
	
	this.mean = function(){
		return this.location;
	};
	
	this.variance = function(){
		return Math.pow(this.scale, 2);
	};
}
HyperbolicSecantDistribution.prototype = new Distribution();

//Irwin-Hall distribution
function IrwinHallDistribution(terms){
	this.terms = terms;
	this.minValue = 0;
	this.maxValue = this.terms;
	this.step = (this.maxValue - this.minValue) / 100;
	this.data = new Data(this.minValue, this.maxValue, this.step);
	this.type = 1;
	
	this.mode = function(){
		return this.terms / 2;
	};
	
	this.density = function(x){
		var sum = 0;
		if (terms == 1) return 1;
		else {
			for (var k = 0; k <= this.terms; k++) sum = sum + Math.pow(-1, k) * binomial(terms, k)*  Math.pow(x - k, this.terms - 1) * sgn(x - k);
			return sum / (2 * factorial(this.terms - 1));
		}
	};
	
	this.simulate = function(){
		var sum = 0;
		for (var i = 0; i < this.terms; i++) sum = sum + Math.random();
		this.setValue(sum);
		return sum;
	};
	
	this.mean = function(){
		return this.terms / 2;
	};
	
	this.variance = function(){
		return this.terms / 12;
	};
	
}
IrwinHallDistribution.prototype = new Distribution();

function LaplaceDistribution(location, scale){
	this.location = location;
	this.scale = scale;
	this.minValue = this.location - 5 * this.scale;
	this.maxValue = this.location + 5 * this.scale; 
	this.step = (this.maxValue - this.minValue) / 100;
	this.data = new Data(this.minValue, this.maxValue, this.step);
	this.type = 1;
	
	this.mode = function(){
		return this.location;
	};
	
	this.density = function(x){
		return Math.exp(-Math.abs(x - this.location) / this.scale) / (2 * this.scale);
	};
	
	this.mean = function(){
		return this.location;
	};

	this.variance = function(){
		return 2 * Math.pow(this.scale, 2);
	};	
	
	this.quantile = function(p){
		if (p <= 0.5) return this.location + this.scale * Math.log(2 * p);
		else return this.location - this.scale * Math.log(2 * (1 - p));
	};
	
	this.CDF = function(x){
		if (x <= this.location) return 0.5 * Math.exp((x - this.location) / this.scale);
		else return 1 - 0.5 * Math.exp(-(x - this.location) / this.scale);
	};
}

LaplaceDistribution.prototype = new Distribution();

function BenfordMantissaDistribution(b){
	this.base = b;
	this.minValue = 1 / this.base;
	this.maxValue = 1;
	this.step = (this.maxValue - this.minValue) / 100;
	this.type = 1;
	this.data = new Data(this.minValue, this.maxValue, this.step);
	
	this.mode = function(){
		return this.minValue;
	};
	
	this.density = function(x){
		return 1 / (x * Math.log(this.base));
	};
	
	this.CDF = function(x){
		return 1 + Math.log(x) / Math.log(this.base);
	};
	
	this.quantile = function(p){
		return 1 / Math.pow(this.base, 1 - p);
	};
	
	this.mean = function(){
		return (this.base - 1) / (this.base * Math.log(this.base));
	};
	
	this.variance = function(){
		return ((this.base - 1) / (Math.pow(this.base, 2) * Math.log(this.base))) * ((this.base + 1) / 2 - (this.base - 1) / Math.log(this.base));
	};
}	
	
BenfordMantissaDistribution.prototype = new Distribution();

function BenfordDigitDistribution(b){
	this.base = b;
	this.minValue = 1;
	this.maxValue = this.base - 1;
	this.step = 1;
	this.type = 0;
	this.data = new Data(this.minValue, this.maxValue, this.step);
	
	this.mode = function(){
		return this.minValue;
	};
	
	this.density = function(x){
		return (Math.log(x + 1) - Math.log(x)) / Math.log(this.base);
	};
}	
BenfordDigitDistribution.prototype = new Distribution();

function BetaBinomialDistribution(a, b, n){
	this.trials = n;
	this.left = a;
	this.right = b;
	this.minValue = 0;
	this.maxValue = this.trials;
	this.step = 1;
	this.type = 0;
	this.data = new Data(this.minValue, this.maxValue, this.step);
	

	this.density = function(x){
		return binomial(this.trials, x) * risePow(this.left, x) * risePow(this.right, this.trials - x) / risePow(this.left + this.right, this.trials);
	};
	
	
	this.mean = function(){
		return this.trials * (this.left / (this.left + this.right));
	};
	
	this.variance = function(){
		return (this.trials * this.left * this.right / Math.pow(this.left + this.right, 2)) * (1 + (this.trials - 1) / (this.left + this.right + 1));
	};
}	
	
BetaBinomialDistribution.prototype = new Distribution();

function BetaNegativeBinomialDistribution(a, b, k){
	this.successes = k;
	this.left = a;
	this.right = b;
	var mu = this.successes * (this.left + this.right - 1) / (this.left - 1);
	var sigma2 = this.successes * (this.left + this.right - 1) * (this.right + this.successes * (this.left + this.right - 2))/ ((this.left - 1) * (this.left - 2)) - mu * mu;
	this.minValue = this.successes;
	this.maxValue = mu + 4 * Math.sqrt(sigma2);
	this.step = 1;
	this.type = 0;
	this.data = new Data(this.minValue, this.maxValue, this.step);
	

	this.density = function(x){
		return binomial(x - 1, this.successes - 1) * risePow(this.left, this.successes) * risePow(this.right, x - this.successes) / risePow(this.left + this.right, x);
	};
	
	
	this.mean = function(){
		return mu;
	};
	
	this.variance = function(){
		return sigma2;
	};
}	
	
BetaNegativeBinomialDistribution.prototype = new Distribution();

//Discrete Uniform distribution
function DiscreteUniformDistribution(a, b){
	this.minValue = a;
	this.maxValue = b;
	this.step = 1;
	this.type = 0;
	this.data = new Data(this.minValue, this.maxValue, this.step);
	var n = this.maxValue - this.minValue + 1;
	
	this.density = function(x){
		if (x < this.minValue || x > this.maxValue) return 0;
		else return 1 / n;
	};
	
	this.mode = function(){
		return this.minValue;
	};
		
	this.CDF = function(x){
		x = Math.round(x);
		if (x < this.minValue) return 0;
		else if (x > this.maxValue) return 1;
		else return (x - this.minValue + 1) / n;
	};
	
	this.simulate = function(){
		var x = this.minValue + Math.floor(n * Math.random());
		this.setValue(x);
		return x;
	};

	this.mean = function(){
		return (this.minValue + this.maxValue) / 2;
	};
	
	this.variance = function(){
		return (this.maxValue - this.minValue) * (this.maxValue - this.minValue + 2) / 12;
	};
}	
DiscreteUniformDistribution.prototype = new Distribution();

//Exponential-Logarithmi distribution
function ExponentialLogarithmicDistribution(shape, scale){
	this.scale = scale;
	this.shape = shape;
	this.minValue = 0;
	this.maxValue = 4 / this.scale;
	this.step = (this.maxValue - this.minValue) / 100;
	this.data = new Data(this.minValue, this.maxValue, this.step);
	this.type = 1;
	
	this.mode = function(){
		return 0;
	};
	
	this.density = function(x){
		return -this.scale * (1 - this.shape) * Math.exp(-this.scale * x) / (Math.log(this.shape) * (1 - (1 - this.shape) * Math.exp(-this.scale * x)));
	};
	
	this.CDF = function(x){
		return 1 - Math.log(1 - (1 - this.shape) * Math.exp(-this.scale * x)) / Math.log(this.shape);
	};
	
	this.quantile = function(p){
		return Math.log((1 - this.shape) / (1 - Math.pow(this.shape, 1 - p))) / this.scale;
	};
	
	this.mean = function(){
		return -polyLog(2, 1 - this.shape) / (this.scale * Math.log(this.shape));
	}
	
	this.variance = function(){
		return -2 * polyLog(3, 1 - this.shape) / (Math.pow(this.scale, 2) * Math.log(this.shape)) - Math.pow(this.mean(), 2);
	}
}
ExponentialLogarithmicDistribution.prototype = new Distribution();

//Beta prime distribution
function BetaPrimeDistribution(a, b){
	this.shape1 = a;
	this.shape2 = b;
	var c = gamma(this.shape1 + this.shape2) / (gamma(this.shape1) * gamma(this.shape2));
	if (this.shape1 >= 1) this.minValue = 0;
	else this.minValue = 0.01;
	this.maxValue = 4 * this.shape1 / this.shape2;
	this.step = (this.maxValue - this.minValue) / 100;
	this.data = new Data(this.minValue, this.maxValue, this.step);
	this.type = 1;
	
	this.mode = function(){
		if (this.shape1 >=1) return (this.shape1 - 1) / (this.shape2 + 1);
		else return this.minValue;
	};
	
	this.density = function(x){
		return c * Math.pow(x, this.shape1 - 1) * Math.pow(1 + x, -this.shape1 - this.shape2);
	};
	
	this.CDF = function(x){
		return betaCDF(x / (x + 1), this.shape1, this.shape2);
	};
	
	this.mean = function(){
		if (this.shape2 > 1) return this.shape1 / (this.shape2 - 1);
		else return Infinity;
	}
	
	this.variance = function(){
		if (this.shape2 > 2) return this.shape1 * (this.shape1 + this.shape2 - 1) / ((this.shape2 - 2) * Math.pow(this.shape2 - 1, 2)) ;
		else if (this.shape2 > 1) return Infinity;
		else return NaN;
	}
}
BetaPrimeDistribution.prototype = new Distribution();

function ZetaDistribution(a){
	this.shape = a;
	this.minValue = 1;
	this.maxValue = Math.ceil(Math.pow(10, 3.5 / this.shape));
	this.step = 1;
	this.type = 0;
	this.data = new Data(this.minValue, this.maxValue, this.step);
	var c = zeta(this.shape);

	this.density = function(x){
		return 1 / (c * Math.pow(x, this.shape));
	};
	
	
	this.mean = function(){
		if (this.shape > 2) return zeta(this.shape - 1) / zeta(this.shape);
		else return Infinity;
	};
	
	this.variance = function(){
		if (this.shape > 3) return zeta(this.shape - 2) / zeta(this.shape) - this.mean() * this.mean();
		else if (this.shape > 2) return Infinity;
		else return NaN;
	};
}	
	
ZetaDistribution.prototype = new Distribution();

function LogarithmicDistribution(p){
	this.shape = p;
	this.minValue = 1;
	var mu = -this.shape / (Math.log(1 - this.shape) * (1 - this.shape));
	var s2 = -this.shape * (this.shape + Math.log(1 - this.shape)) / (Math.pow(1 - this.shape, 2) * Math.pow(Math.log(1 - this.shape), 2));
	this.maxValue = mu + 4 * Math.sqrt(s2);
	this.step = 1;
	this.type = 0;
	this.data = new Data(this.minValue, this.maxValue, this.step);

	this.density = function(x){
		return -Math.pow(this.shape, x) / (x * Math.log(1 - this.shape));
	};
	
	this.mode = function(){
		return 1;
	};
	
	this.mean = function(){
		return mu;
	};
	
	this.variance = function(){
		return s2;
	};
}	
	
LogarithmicDistribution.prototype = new Distribution();

function LogLogisticDistribution(a, b){
	this.scale = a;
	this.shape = b;
	if (this.shape >= 1) this.minValue = 0;
	else this.minValue = 0.001;
	if (this.shape >= 2) this.maxValue = this.scale * Math.pow(100, 1 / this.shape);
	else this.maxValue = this.scale * 10;
	this.step = (this.maxValue -this.minValue) / 100;
	this.type = 1;
	this.data = new Data(this.minValue, this.maxValue, this.step);

	this.density = function(x){
		return (this.shape / this.scale) * Math.pow(x / this.scale, this.shape - 1) / Math.pow(1 + Math.pow(x / this.scale, this.shape), 2);
	};
	
	this.mode = function(){
		if (this.shape >=1) return this.scale * Math.pow((this.shape - 1) / (this.shape + 1), 1 / this.shape);
		else return this.minValue;
	};
	
	this.mean = function(){
		if (this.shape > 1) return ((this.scale * Math.PI) / this.shape) / Math.sin(Math.PI / this.shape);
		else return Infinity;
	};
	
	this.variance = function(){
		var t = Math.PI / this.shape;
		if (this.shape > 2) return Math.pow(this.scale, 2) * ((2 * t) / Math.sin(2 * t) - Math.pow(t, 2) / Math.pow(Math.sin(t), 2));
		else if (this.shape > 1) return Infinity;
		else return NaN;
	};
	
	this.CDF = function(x){
		return Math.pow(x, this.shape) / (Math.pow(this.scale, this.shape) + Math.pow(x, this.shape));
	};
	
	this.quantile = function(p){
		return this.scale * Math.pow(p / (1 - p), 1 / this.shape);
	};
}	
	
LogLogisticDistribution.prototype = new Distribution();

function MaxwellBoltzmannDistribution(a){
	this.shape = a;
	var mu = 2 * this.shape * Math.sqrt(2 / Math.PI);
	var sigma = this.shape * Math.sqrt(3 - 8 / Math.PI);
	this.minValue = 0;
	this.maxValue = mu + 4 * sigma;
	this.step = (this.maxValue -this.minValue) / 100;
	this.type = 1;
	this.data = new Data(this.minValue, this.maxValue, this.step);

	this.density = function(x){
		return Math.sqrt(2 / Math.PI) * Math.pow(x, 2) * Math.exp(-Math.pow(x, 2) / (2 * Math.pow(this.shape, 2))) / Math.pow(this.shape, 3);
	};
	
	this.mode = function(){
		return Math.sqrt(2) * this.shape;
	};
	
	this.mean = function(){
		return mu;
	};
	
	this.variance = function(){
		return sigma * sigma;
	};
}	
	
MaxwellBoltzmannDistribution.prototype = new Distribution();

function UQuadraticDistribution(a, b){
	this.left = a;
	this.right = b;
	this.minValue = a;
	this.maxValue = b;
	this.step = (this.maxValue -this.minValue) / 100;
	this.type = 1;
	this.data = new Data(this.minValue, this.maxValue, this.step);

	this.density = function(x){
		return (12 / Math.pow(this.right - this.left, 3)) * Math.pow(x - (this.left + this.right) / 2, 2) ;
	};
	
	this.CDF = function(x){
		return (4 / Math.pow(this.right - this.left, 3)) * Math.pow(x - (this.left + this.right) / 2, 3) + 1/2;
	};
	
	this.mode = function(){
		return this.left;
	};
	
	this.mean = function(){
		return (this.left + this.right) / 2;
	};
	
	this.variance = function(){
		return 3 * Math.pow(this.right - this.left, 2) / 20;
	};
}	
	
UQuadraticDistribution.prototype = new Distribution();
