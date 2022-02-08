/**
 * Minified by jsDelivr using Terser v5.7.1.
 * Original file: /npm/discrete-wavelets@5.0.10/dist/discrete-wavelets.umd.js
 *
 * Do NOT use SRI with dynamically generated files! More information: https://www.jsdelivr.com/using-sri-with-dynamic-files
 */
 ! function(t, e) {
    "object" == typeof exports && "undefined" != typeof module ? module.exports = e() : "function" == typeof define && define.amd ? define(e) : (t = "undefined" != typeof globalThis ? globalThis : t || self).wt = e()
}(this, (function() {
    "use strict";
    var t = "symmetric";

    function e(t, e, n) {
        if (void 0 === n && (n = !1), 0 === t.length) throw new Error("Cannot determine symmetric padding for data of zero length.");
        var r = Math.floor(e / t.length);
        return (n ? r : r + 1) % 2 == 0 ? t[e % t.length] : t[t.length - 1 - e % t.length]
    }
    var n = "antisymmetric";
    var r = "constant";
    var o = "periodic";
    var i = "reflect";
    var a = "smooth";
    var h = "zero";
    var l = {
            antisymmetric: n,
            constant: r,
            periodic: o,
            reflect: i,
            smooth: a,
            symmetric: t,
            zero: h,
            modes: [h, r, t, o, a, i, n]
        },
        f = [1 / Math.SQRT2, 1 / Math.SQRT2],
        c = [(1 + Math.sqrt(3)) / (4 * Math.SQRT2), (3 + Math.sqrt(3)) / (4 * Math.SQRT2), (3 - Math.sqrt(3)) / (4 * Math.SQRT2), (1 - Math.sqrt(3)) / (4 * Math.SQRT2)],
        s = [.33267055295008263, .8068915093110925, .45987750211849154, -.13501102001025458, -.08544127388202666, .03522629188570953],
        u = [.2303778133088965, .7148465705529157, .6308807679298589, -.027983769416859854, -.18703481171909309, .030841381835560764, .0328830116668852, -.010597401785069032],
        d = [.16010239797419293, .6038292697971896, .7243085284377729, .13842814590132074, -.24229488706638203, -.032244869584638375, .07757149384004572, -.006241490212798274, -.012580751999081999, .0033357252854737712],
        g = [.11154074335010947, .49462389039845306, .7511339080210954, .31525035170919763, -.22626469396543983, -.12976686756726194, .09750160558732304, .027522865530305727, -.03158203931748603, .0005538422011614961, .004777257510945511, -.0010773010853084796],
        w = [.07785205408500918, .3965393194819173, .7291320908462351, .4697822874051931, -.14390600392856498, -.22403618499387498, .07130921926683026, .08061260915108308, -.03802993693501441, -.01657454163066688, .01255099855609984, .0004295779729213665, -.0018016407040474908, .00035371379997452024],
        v = [.05441584224310401, .31287159091429995, .6756307362972898, .5853546836542067, -.015829105256349306, -.2840155429615469, .0004724845739132828, .12874742662047847, -.017369301001807547, -.044088253930794755, .013981027917398282, .008746094047405777, -.004870352993451574, -.00039174037337694705, .0006754494064505693, -.00011747678412476953],
        m = [.038077947363878345, .24383467461259034, .6048231236901112, .6572880780513005, .13319738582500756, -.2932737832791749, -.09684078322297646, .14854074933810638, .03072568147933338, -.06763282906132997, .00025094711483145197, .022361662123679096, -.004723204757751397, -.00428150368246343, .0018476468830562265, .00023038576352319597, -.0002519631889427101, 393473203162716e-19],
        p = [.026670057900555554, .1881768000776915, .5272011889317256, .6884590394536035, .2811723436605775, -.24984642432731538, -.19594627437737705, .12736934033579325, .09305736460357235, -.07139414716639708, -.029457536821875813, .033212674059341, .0036065535669561697, -.010733175483330575, .001395351747052901, .001992405295185056, -.0006858566949597116, -.00011646685512928545, 9358867032006959e-20, -13264202894521244e-21],
        b = {
            db1: f,
            db2: c,
            db3: s,
            db4: u,
            db5: d,
            db6: g,
            db7: w,
            db8: v,
            db9: m,
            db10: p,
            D2: f,
            D4: c,
            D6: s,
            D8: u,
            D10: d,
            D12: g,
            D14: w,
            D16: v,
            D18: m,
            D20: p,
            haar: f
        };

    function y(t, e) {
        if (t.length !== e.length) throw new Error("Both arrays have to have the same length.");
        return t.map((function(t, n) {
            return t + e[n]
        }))
    }

    function E(t) {
        if (t.high.length !== t.low.length) throw new Error("High-pass and low-pass filters have to have equal length.");
        if (t.low.length < 2) throw new Error("Wavelet filter length has to be larger than or equal to two.");
        return !0
    }

    function M(t) {
        return "string" != typeof t ? t : function(t) {
            if (t.length < 2) throw new Error("Scaling numbers length has to be larger than or equal to two.");
            var e = t.slice().reverse().map((function(t, e) {
                return e % 2 == 0 ? t : -t
            }));
            return {
                dec: {
                    low: t.slice(),
                    high: e.slice()
                },
                rec: {
                    low: t.slice(),
                    high: e.slice()
                }
            }
        }(b[t])
    }

    function z(t, e) {
        if (void 0 === e && (e = 0), !Number.isInteger(t)) throw new Error("Length has to be an integer.");
        if (t < 0) throw new Error("Length must not be smaller than zero.");
        return Array.apply(null, Array(t)).map((function(t, n) {
            return "function" == typeof e ? e(n) : e
        }))
    }

    function D(t, e) {
        if (t.length !== e.length) throw new Error("Both arrays have to have the same length.");
        return t.reduce((function(t, n, r) {
            return t + n * e[r]
        }), 0)
    }

    function q(t, e) {
        return e.map((function(e) {
            return t * e
        }))
    }

    function C(t, n, r, o) {
        switch (o) {
            case l.antisymmetric:
                return function(t, n, r) {
                    return void 0 === r && (r = !1), (Math.floor(n / t.length) % 2 == 0 ? -1 : 1) * e(t, n, r)
                }(t, n, r);
            case l.constant:
                return function(t, e) {
                    if (void 0 === e && (e = !1), 0 === t.length) throw new Error("Cannot determine constant padding for data of zero length.");
                    return e ? t[0] : t[t.length - 1]
                }(t, r);
            case l.periodic:
                return function(t, e, n) {
                    if (void 0 === n && (n = !1), 0 === t.length) throw new Error("Cannot determine periodic padding for data of zero length.");
                    return n ? t[t.length - 1 - e % t.length] : t[e % t.length]
                }(t, n, r);
            case l.reflect:
                return function(t, e, n) {
                    if (void 0 === n && (n = !1), 0 === t.length) throw new Error("Cannot determine reflect padding for data of zero length.");
                    if (1 === t.length) return t[0];
                    var r = Math.floor(e / (t.length - 1));
                    return (n ? r : r + 1) % 2 == 0 ? t[e % (t.length - 1) + 1] : t[t.length - 2 - e % (t.length - 1)]
                }(t, n, r);
            case l.smooth:
                return function(t, e, n) {
                    if (void 0 === n && (n = !1), 0 === t.length) throw new Error("Cannot determine smooth padding for data of zero length.");
                    var r = t.length - 1;
                    return (n ? t[0] : t[r]) + (e + 1) * (n ? 1 === t.length ? t[0] : t[0] - t[1] : 1 === t.length ? -t[0] : t[r] - t[r - 1])
                }(t, n, r);
            case l.symmetric:
                return e(t, n, r);
            case l.zero:
                return 0;
            default:
                throw new Error('Unknown signal extension mode: "' + o + '"')
        }
    }
    var T = l.symmetric;
    return function() {
        function t() {}
        return t.dwt = function(t, e, n) {
            void 0 === n && (n = T);
            var r = M(e).dec;
            E(r);
            var o = r.low.length;
            t = this.pad(t, function(t, e) {
                if (t <= 0) throw new Error("Cannot determine padding widths for data of length less than or equal to zero.");
                if (e < 2) throw new Error("Cannot determine padding widths for filter of length less than two.");
                return [e - 2, (t + e) % 2 == 0 ? e - 2 : e - 1]
            }(t.length, o), n);
            for (var i = [], a = [], h = 0; h + o <= t.length; h += 2) {
                var l = t.slice(h, h + o);
                i.push(D(l, r.low)), a.push(D(l, r.high))
            }
            return [i, a]
        }, t.energy = function(t) {
            for (var e = 0, n = 0, r = t; n < r.length; n++) {
                var o = r[n];
                e += "number" == typeof o ? Math.pow(o, 2) : this.energy(o)
            }
            return e
        }, t.idwt = function(t, e, n) {
            if (void 0 === t && void 0 !== e && (t = z(e.length, 0)), void 0 === e && void 0 !== t && (e = z(t.length, 0)), void 0 === t || void 0 === e) throw new Error("Coefficients must not be undefined.");
            ! function(t, e) {
                if (t.length !== e.length) throw new Error("Approximation and detail coefficients must have equal length.");
                if (0 === t.length) throw new Error("Approximation and detail coefficients must not have zero length.")
            }(t, e);
            var r = M(n).rec;
            E(r);
            for (var o = r.low.length, i = t.length, a = z(o + 2 * (i - 1), 0), h = 0; h < i; h++) {
                var l = 2 * h,
                    f = a.slice(l, l + o);
                f = y(f, q(t[h], r.low)), f = y(f, q(e[h], r.high)), a = a.slice(0, l).concat(f).concat(a.slice(l + f.length))
            }
            return a.slice(o - 2, a.length - (o - 2))
        }, t.maxLevel = function(t, e) {
            if (!Number.isInteger(t)) throw new Error("Length of data is not an integer. This is not allowed.");
            if (t < 0) throw new Error("Data length cannot be less than zero.");
            if (0 === t) return 0;
            var n = M(e).dec.low.length;
            return Math.max(0, Math.floor(Math.log2(t / (n - 1))))
        }, t.pad = function(t, e, n) {
            if (!t) throw new Error("Cannot add padding to empty data.");
            var r = e[0],
                o = e[1];
            return z(r, (function(e) {
                return C(t, r - 1 - e, !0, n)
            })).concat(t).concat(z(o, (function(e) {
                return C(t, e, !1, n)
            })))
        }, t.wavedec = function(t, e, n, r) {
            if (void 0 === n && (n = T), void 0 === r && (r = this.maxLevel(t.length, e)), r < 0) throw new Error("Decomposition level must not be less than zero");
            for (var o = [], i = t.slice(), a = 1; a <= r; a++) {
                var h = this.dwt(i, e, n);
                i = h[0];
                var l = h[1];
                o.unshift(l.slice())
            }
            return o.unshift(i.slice()), o
        }, t.waverec = function(t, e) {
            ! function(t) {
                if (t.length < 1) throw new Error("Invalid coefficients. Array length must not be zero.")
            }(t), e = M(e);
            for (var n = t[0], r = 1; r < t.length; r++) {
                var o = t[r];
                n.length === o.length + 1 && (n = n.slice(0, n.length - 1)), n = this.idwt(n, o, e)
            }
            return n.slice()
        }, t.Modes = l, t
    }()
}));
//# sourceMappingURL=/sm/76e324152eadd7fa8ba3d1382edb4adc8f90e65dee9aaff6fe039eb41f606ade.map