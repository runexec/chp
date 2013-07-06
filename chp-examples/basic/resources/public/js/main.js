var e = null;
function f(a) {
  var b = typeof a;
  if("object" == b) {
    if(a) {
      if(a instanceof Array) {
        return"array"
      }
      if(a instanceof Object) {
        return b
      }
      var c = Object.prototype.toString.call(a);
      if("[object Window]" == c) {
        return"object"
      }
      if("[object Array]" == c || "number" == typeof a.length && "undefined" != typeof a.splice && "undefined" != typeof a.propertyIsEnumerable && !a.propertyIsEnumerable("splice")) {
        return"array"
      }
      if("[object Function]" == c || "undefined" != typeof a.call && "undefined" != typeof a.propertyIsEnumerable && !a.propertyIsEnumerable("call")) {
        return"function"
      }
    }else {
      return"null"
    }
  }else {
    if("function" == b && "undefined" == typeof a.call) {
      return"object"
    }
  }
  return b
}
;function g(a, b) {
  var c = Array.prototype.slice.call(arguments), d = c.shift();
  if("undefined" == typeof d) {
    throw Error("[goog.string.format] Template required");
  }
  return d.replace(/%([0\-\ \+]*)(\d+)?(\.(\d+))?([%sfdiu])/g, function(a, b, d, s, G, t, H, I) {
    if("%" == t) {
      return"%"
    }
    var u = c.shift();
    if("undefined" == typeof u) {
      throw Error("[goog.string.format] Not enough arguments");
    }
    arguments[0] = u;
    return g.a[t].apply(e, arguments)
  })
}
g.a = {};
g.a.s = function(a, b, c) {
  return isNaN(c) || "" == c || a.length >= c ? a : a = -1 < b.indexOf("-", 0) ? a + Array(c - a.length + 1).join(" ") : Array(c - a.length + 1).join(" ") + a
};
g.a.f = function(a, b, c, d, j) {
  d = a.toString();
  isNaN(j) || "" == j || (d = a.toFixed(j));
  var h;
  h = 0 > a ? "-" : 0 <= b.indexOf("+") ? "+" : 0 <= b.indexOf(" ") ? " " : "";
  0 <= a && (d = h + d);
  if(isNaN(c) || d.length >= c) {
    return d
  }
  d = isNaN(j) ? Math.abs(a).toString() : Math.abs(a).toFixed(j);
  a = c - d.length - h.length;
  return d = 0 <= b.indexOf("-", 0) ? h + d + Array(a + 1).join(" ") : h + Array(a + 1).join(0 <= b.indexOf("0", 0) ? "0" : " ") + d
};
g.a.d = function(a, b, c, d, j, h, E, s) {
  return g.a.f(parseInt(a, 10), b, c, d, 0, h, E, s)
};
g.a.i = g.a.d;
g.a.u = g.a.d;
function i(a) {
  return a
}
var k = ["cljs", "core", "set_print_fn_BANG_"], l = this;
!(k[0] in l) && l.execScript && l.execScript("var " + k[0]);
for(var m;k.length && (m = k.shift());) {
  var n;
  if(n = !k.length) {
    n = void 0 !== i
  }
  n ? l[m] = i : l = l[m] ? l[m] : l[m] = {}
}
function p(a) {
  var b = "string" == typeof a;
  return b ? "\ufdd0" !== a.charAt(0) : b
}
function q(a) {
  var b = r;
  return b[f(a == e ? e : a)] ? !0 : b._ ? !0 : !1
}
function v(a) {
  var b = a == e ? e : a.constructor;
  return Error(["No protocol method ILookup.-lookup defined for type ", (b != e && !1 !== b ? b.k : b) != e && !1 !== (b != e && !1 !== b ? b.k : b) ? b.l : f(a), ": ", a].join(""))
}
var r = {}, w, x = e;
function y(a, b) {
  if(a ? a.e : a) {
    return a.e(a, b)
  }
  var c;
  c = w[f(a == e ? e : a)];
  if(!c && (c = w._, !c)) {
    throw v(a);
  }
  return c.call(e, a, b)
}
function z(a, b, c) {
  if(a ? a.g : a) {
    return a.g(a, b, c)
  }
  var d;
  d = w[f(a == e ? e : a)];
  if(!d && (d = w._, !d)) {
    throw v(a);
  }
  return d.call(e, a, b, c)
}
x = function(a, b, c) {
  switch(arguments.length) {
    case 2:
      return y.call(this, a, b);
    case 3:
      return z.call(this, a, b, c)
  }
  throw Error("Invalid arity: " + arguments.length);
};
x.b = y;
x.c = z;
w = x;
var A, B = e;
function C(a, b) {
  var c;
  if(a == e) {
    c = e
  }else {
    if(c = a) {
      c = (c = a.j & 256) ? c : a.h
    }
    c = c ? a.e(a, b) : a instanceof Array ? b < a.length ? a[b] : e : p(a) ? b < a.length ? a[b] : e : q(a) ? w.b(a, b) : e
  }
  return c
}
function D(a, b, c) {
  if(a != e) {
    var d;
    if(d = a) {
      d = (d = a.j & 256) ? d : a.h
    }
    a = d ? a.g(a, b, c) : a instanceof Array ? b < a.length ? a[b] : c : p(a) ? b < a.length ? a[b] : c : q(a) ? w.c(a, b, c) : c
  }else {
    a = c
  }
  return a
}
B = function(a, b, c) {
  switch(arguments.length) {
    case 2:
      return C.call(this, a, b);
    case 3:
      return D.call(this, a, b, c)
  }
  throw Error("Invalid arity: " + arguments.length);
};
B.b = C;
B.c = D;
A = B;
var F = e, F = function(a, b, c) {
  switch(arguments.length) {
    case 2:
      return A.b(b, this.toString());
    case 3:
      return A.c(b, this.toString(), c)
  }
  throw Error("Invalid arity: " + arguments.length);
};
String.prototype.call = F;
String.prototype.apply = function(a, b) {
  return a.call.apply(a, [a].concat(b.slice()))
};
String.prototype.apply = function(a, b) {
  return 2 > b.length ? A.b(b[0], a) : A.c(b[0], a, b[1])
};
alert("Hi from ClojureScript!");
