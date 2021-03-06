console.info('>> module "utils" is loaded');
/**
 * @fileOverview 工具类，除业务外的资源获取，参数解析等方法
 */
define(function () {
    var wndProp;
    var graphEditor;
    var uuidArr;
    var cellsToString = function (cells) {
        var codec = new mxCodec();
        var model = new mxGraphModel();
        var parent = model.getChildAt(model.getRoot(), 0);

        for (var i = 0; i < cells.length; i++) {
            model.add(parent, cells[i]);
        }
        return mxUtils.getXml(codec.encode(model));
    };

    //根据名称获取url参数
    var getUrlParam = function (name) {
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
        if (!window.location.search) return null;
        var r = window.location.search.substr(1).match(reg);
        if (r != null) {
            return unescape(r[2]);
        }
        return null;
    };

    var getBaseImgByBtnType = function (btnType) {
        switch (btnType) {
            case 'zoomout':
                return 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAMAAAAoLQ9TAAABVlBMVEUAAADy//+OlZiEy/X/+vnk4+N3l63o5uT+/v6Fnq7/hVTk4+KOzO2FvNyFn6+Hxu2Ex+yaEgDu7u7U09OIjpCZpqb/2m///f14dHP/n0Z7eHf/2mqS0fO8u7uxzNWT0/aHqcGJobH29/aRoKiFyPHs6+uJttKx6/+kFQDKyMfY1tbS8/vRz8+lxNiNts+c1O2QlZjf6e6BoLP2kkP+g1W04va0sa/8+Pee2feykXCU1fqOzvX/9/X/l23/+fjw7+/p5uSOy+zy8vLX1tWmqayZoaSM0Pyh3/7/mkOPDgCXo6h1BQD/rFCWpav1rV3c///L2+CwusCPpbK3u7/L9/+ssLGDnbD6+vrK9v//qk58qsjw9//a2tr/iFuP0v2koaGbmpe9k2v/02GuytqTmp2Szu7z8/P/lWy2z9a56/+4z9yrrbCEq8P9iWCgpaSwrKr///+DCgAyi0cqAAAAAXRSTlMAQObYZgAAAAFiS0dEAIgFHUgAAAAJcEhZcwAACxMAAAsTAQCanBgAAAAHdElNRQfhCw8JNTnMmjLxAAAAnElEQVQY02NgwAQFOnleKflOcD6HbIZ/hLq+gRBMQEuOkVHb1EI+HibgEsLuEJCla8VnDxXwzdQQ9Cz05tVzhgq4uicHKYaxRSmwQgXMHGVSeQT445hgZqQxMTOrWLtxSgWaQ0VUE4zUckQkohNNWGCKuDWFw4MtkyJj7ZDcq5QrKebDZYMkIp7uIe2njOIpW2PDbFRviobGYPodAKKEGTChy0DTAAAAAElFTkSuQmCC';
            case 'zoomin':
                return 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAMAAAAoLQ9TAAABUFBMVEUA8D+Ey/UKlgYAgQCOlZix4sj/hVTk4+P/////+vn+/v7/lWzu7u4AkQC77dPs6+uFnq7X1tWS0fP//f0IlQUJlgWZoaST0/aZpqav48HKyMe8u7uP0v3/n0a868WOy+yJttKWpavRz8//iFuIjpCykXCkoaHa2tqM0Px+t9qXo6ic1O3K6c/29/b/2m94dHPL6tD8+Pf+g1X6+vqxzNXb/dnL9//9iWC86Mr/qk6BoLPf/9nc//+Fn6+x6//k4+L/2mq/79Pf6e6Nts//l223u7/H5897eHcJlQb/mkPy//+RoKjB5tf1rV32kkOQlZiHu9v/02GHqcGmqayU1frw9//Y1taEq8MAkwCssLHU09O2z9aq4MH/9/WgpaS9k2vK8MTw7++wusCTmp3y8vKh3/7/rFC0sa+bmpewrKqe2ffz8/PB78WrrbD/+fiHxu1h2uY9AAAAAXRSTlMAQObYZgAAAAFiS0dEAIgFHUgAAAAJcEhZcwAACxMAAAsTAQCanBgAAAAHdElNRQfhCw8JNg5fCsQ9AAAAnElEQVQY02NgwAQcSnFayZkpcD6XdLRNjp22Pw9MIMzEyzrCJ0tcDSYQbGaawGTgGGKbCBVQlOP1EBVhlnQWhAqIpVroMLmxBgSxQwXS5YX4mFnzZVhgZmSzMDLGaGoIuCcZQkX4M6wUwlX0Q+ONOGGK7KWijF1VAy2V85Dcq2suq5fGFoskIszt6eDrjeIpFz+nXFRvSkSqY/odAA1jGFh1WxV0AAAAAElFTkSuQmCC';
            case 'zoomactual':
                return 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAMAAAAoLQ9TAAABXFBMVEUAAACOlZj/l229vLwVHSLI6ff///+m2/2k2vy8u7uJttKIj5Lz8/MaIyccIiT/hVSq3vuq3Pi+5fUgJiiq2vap2PKDiYyEy/W13vSa0e6gnpux3fEhJCTX1tWEq8Pa2tr/2mrY1tassLGrrbDs+/2Nts/29/aNtML0//93fX1NanlIZndCYXT2kkOFyPH//f3/2m//mkOwusD8+Pf/9/XN7/mFnq79iWCSkJD/02GFk5j/iFuPkpL/lWydmZfu7u7/+fibmpf/rFDy8vKQlZiUl5nRz8//n0aOzvUYHyP6+vqr4/7s6+u3u7/D6Pj1rV17eHd4lqzR+P/Lysrw9//S8/u60trf6e6zsrG64/S+1Nup1/K9k2v+/v4WHiPB7f/k4+ORoKiZpqax6/+OzO14dHOGkZXK9v+IjpD+g1Xw7++BoLP/qk6ykXD/+vme1vet2/jc//8UHCJ3jZyfqW55AAAAAXRSTlMAQObYZgAAAAFiS0dEAIgFHUgAAAAJcEhZcwAACxMAAAsTAQCanBgAAAAHdElNRQfhCw8JNhpF0BBAAAAAnElEQVQY02NgwARsbjZpYnbOcH4sZ1RherK0iz1MQDFMUyPUT0vQAibgGiSjIswaJ1CcBRWwiuczVRdiEVGVhQpwe3tG8koUFQQmQAWkJLVFU6J12BlhZvAw5ovreXCYBRgZQ0V8HLO55DJSQ2Iy82CKgiOYvXxzLXOsHZDcq2bubuDEb4Ikom9rqOCfiOIpJt1wZVRvJinJY/odAP2wGhAs1O1vAAAAAElFTkSuQmCC';
            case 'undo':
                return 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAMAAAAoLQ9TAAABX1BMVEUAAACOyv/H2/TD4v+x2P+y3P+z3v/W5Pjx9/654P92vv8haMN1u/4jZsDz9vuFwfyEwv+u2f+Uyv+fx/NipeYReOoketQtiOB6v/+Nx/8ykdxYpu8UdNtsuf9is//v9v0rcMQ5b78XgOqEst87qf+eye5GmNmQx/+Dxf/2+fybz/9MqvCJw/Gfz/2HzP+qzO+VuOa00fA0hNM8meo4kOExkeSDteKJrNx1wf+PsuJIqf9FiM+YzP+Oyf8iov+z2v+HrduLxP8th9lJf8WWweqbueJpp+MvgNRYouhOesEqhNh+rN0qecvp7/mQteQTaM5bnNxMidIyof9Ek9Y6gNdLoOo0jdmGwv8PaM+Zzf+YueYQdeNYr//9/v5BqP+Awf9rkthAkNkbaMgvfdEjnf9wlcyBt+tcsf+Uxvb5+/2U0P9hlNZDgdBUnNpQoeiazP+p1f85kNuazPvT4/lRl9xW1tziAAAAAXRSTlMAQObYZgAAAAFiS0dEAIgFHUgAAAAJcEhZcwAACxMAAAsTAQCanBgAAAAHdElNRQfhCw8JNiJt0qjeAAAAo0lEQVQY02NgIBb4pipwK5oj+Ew+kVkWKqIJMH6xO1uMVVxQSrQrkJPmkalpxCmXLhaYI2PnCRRIFFL21pWQTTZkYPBTSgIKBLMWhglymeiDtIZEAIlcewH+eJ4AsFnZ/kDCrEgyXMvJukQYyLbkBRIueQWO+TYaeuIcDAxRzkCBWGMWdVtGaS8RINvAAaRRmzkj1NRNHsRk5wOROlJqqgwkAADGaxqD/n56BAAAAABJRU5ErkJggg==';
            case 'saveas':
                return "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAMAAAAoLQ9TAAABm1BMVEU5V7hvb2/39/eMjIzOzs7b29vf399ubm729vbr6+vR0dHl5eXo6OhoaGh5eXkRTKsSTanl6OnQ0ND6+vqVlZXu6d707ucHXcAALJocYLc7Z6wvhtE4b7PLy8vq4tsnc8MLY8QgZrl6enpfi8UWaMLU2eAzMzPWzcLt5+H17+SUweQ8eLi7v8TIwLeXl5cUXbkfdswBWsMUVK/q380RVLEfe87o3tYVZboydsfGyMoBRarn5+e2vtDYxrUOackPT5Hw8PDi4+UYcsr07+cVZbvr6urx8fE5crQ2bbASTqsaY7nt5uDk5OT8+/sTYbwAJJETTqwQa8no4dc9b7AlbL5+fn7Wz8cAM57Q0tMWX7Viod/Dv7oHWr/Jy8zk5ebX19f29fV3d3fPz8+Oq9jTyr8USqc2a68OZcbZ2t2SuNsrgdCkpKQJYsMYccgcbL7m4NcYZrsBSLfe08oadMrExcpFgLkTTar48uokeMf58+fNzc3x6+S6vL4APKMRTqrZz8Y5dbYLZMYIX8Hg4OBtbW3Y2NjMzMz///9mZmbz2ULuAAAAAXRSTlMAQObYZgAAAAFiS0dEAIgFHUgAAAAJcEhZcwAACxMAAAsTAQCanBgAAAAHdElNRQfhCw8JNi8TY9RjAAAA4UlEQVQY02OIKsyPE3TMiIjVNVTmEWZgYIjxr2yX5UpicS2yN8rmAQqIS2i2O7hx+nDWO9UlsgIFmsJFfdma2Vit1XMF2FuBAjm1QQlVXEIs3CnF/GABBSvjcmexam8NW9OyFpBAo0lwmoeMe4N2aZYnWCBdUj/Sq8DFPK9COgAsYKfoZ2ajEyZnmamV3BIPFAgMUSnpgINmJgYLeYOajjbeUN42IOhgbmZQlUqN7mhrV2sHgQ7GVgYOFhGljjY+PT6wAMhYDm5WhBmMIHsYGJqZGVvAgBFoBggwNbdCQTMTAFxgSxznQK+2AAAAAElFTkSuQmCC";
            case 'save':
                return 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAMAAAAoLQ9TAAACNFBMVEUAAAD////k5OTQ09YIZcfCw8TAwcIKRqUIWLpCca////j///oMacsJaMjv7+/Oz9DMz9ENaMn07ea9v8G8vb7u7u4NRKS/wMHJzs8HYsYth9UTU7ONnb57lLEJZcgKZ8kORKRssOTo6OgEV8Pz7eRjldvk3tXb3N3m3NGfp67S09Ts5d0Wbsnq4tnLzM0nec0keM4NSKmJnLU/cK7r6+tZh7kfZKVAc7H9/v7p5eIORaUzabEAN54ORaSKmq1Bc6/Nxr8jZ6eorLBMZJDh4eEgec0KackbXbWJnbMfd83DxccUVbCFm7FgfLMZZr91vuwZULEKXb6JoLUPW7gqVa0UT4oDQ6gNcM7JysuQpdH78uYkcK3S0NAAPKIAMZoASa/x8fGxuL+0tLTMzc5Bd7JukLTNzs8JRKT4+PhBgL0XXbbp6em9vb2Knb8NRKWEmbhDerTx7elpr+Uqf9Bsmsbt6uZeh7UocMDi4N67vsGEjpoVP4rm498mf9ASSajl39kOYsJ6r9yxsa0yidMifM8KY8XP0NH7+/vIyswQZ8VCldrS1NXs7OwEW8Nyk9OApdj6+voJWLr17uaKnLwAKJOFm7rr5+QNRaaIm7IthNQthNEORqYdY6P///4cT6yKl6MfWJAdcMJCdrATacgebMPX19UXY76DpMi+v8BVf7AGYsWMo7nv6eGLkZh/tOcfY7jj5OUZYrxGfbRafK4JS6xwh541lN0jaLu/wMJrh6f8+fSoopkPBCHnAAAAAXRSTlMAQObYZgAAAAFiS0dEAIgFHUgAAAAJcEhZcwAACxMAAAsTAQCanBgAAAAHdElNRQfhCw8JNjnnt2EyAAAA70lEQVQY02NgwADrlJcvZWYW6PCqzJHt7QcKtE6RYdzA35amp1W4OlQaKLAqLpexXZQvm4mpNGihIVBA0mYyozobKytb8jQPTwWgAEvsVO6EHhMlJhfLGa55QAG5sPxd3SIrxHdE1Pi0iIFUbDGKUpkktEZbTcOu1hYowOttzWlsb74opWBT5qzZQAF596xgjsCGzsU6BsXNVkABt/UbVwo7ra2ar7nAsWs6UIBnu1+qb3yZQwWj2UzFdKCAYPmSnZsnltRncM2RKmIHCqgu099qGt44d4Ju9Db/OqCARV+Ac1OiREzS7up5IZGYngcAmopExKTtZCwAAAAASUVORK5CYII=';
            case 'refresh':
                return 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAMAAAAoLQ9TAAAA3lBMVEVA/0BRcqldkABekgBjlgBvl8xllfNqmfVxnfV3ovZ9p/ir13K33nKEq/mLsPqQtfuWufyavf2z2I+224++3LCfwP6iwv/H447W66/N5tbW6dTe7NXY4vbc5vfa5//d5fjf6f/e6//n89Pn9dPg5/jh6Pji6v/j6//k6vnk6/nn7v/o7fro7vno7v/q8P/s8Prs8Pvs8v/v8/zv9P/z9vzz9v3z9v/09//0+f/2+P32+f32+P/4+f/5+v/5+/76+v/7+//6/P/6/f/8/P78/f78/f/+/v7+/v/+///////AzwBPAAAAAXRSTlMAQObYZgAAAAFiS0dEAIgFHUgAAAAJcEhZcwAACxMAAAsTAQCanBgAAAAHdElNRQfhCw8JNwpBfDFlAAAAj0lEQVQY013M2Q4BMQCF4Y6j9n2pwcQ2MoQQSyixb0Pf/4VUpTXjv/yScwj5j9FgjBDKWLVYyOeyqWQiHqMSwkkQpufdsX4A4V+PBly7Bpz3Ww0uZFFsVhpgix7AlwsNGYETeHo+Mx9AkzcwGZuJquINv/C6XRApl+pet6/Afxx2a0xHg06rrcAJ9gErFHkD8fEZq/GNCLUAAAAASUVORK5CYII=';
            case 'redo':
                return 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAMAAAAoLQ9TAAABX1BMVEUAAACGyf9SjtUbacqOyf+Px//B2PCyyOuOx/8tn/8ocMpxpt/0+f5vuPz3+v55xv88hdx8w/87itSLvOmOve4ue9I/leSFsuCCuOWLxP8qn/+NsOMvk+NVndl3u/9xu/8dfOQyg9hDld2d0f+Gwv8xju6Cwv9yruqDxP9zvf/h7Plvs+lZg8VAp/9RoOmkx+2q1/8Xdt8wj9oohte12/8jov85meQxitxVkNKq2P88kNd5uveGyP/5+v4witwYgO2OyP9wreUmcMj4+v9ynttuu/95v/+Zzf82d8mbzv+azP+Mrtyx0fBXmthxmdtLnOY7jtS24v9foeGVy/82j9s0i9hbjsxMoOpisv8XbNErf9BXnNxvpt/5+/19vv5cs/+Yz/81cMJJm9pGiNLB0Oqg0P80k+GLy/87lOFkjspLqv/E4PeDsd4dcNOgz/+z0+6Hxf8le9LU5Phyr+dQq/88OvXTAAAAAXRSTlMAQObYZgAAAAFiS0dEAIgFHUgAAAAJcEhZcwAACxMAAAsTAQCanBgAAAAHdElNRQfhCw8JNxJSEKkzAAAApElEQVQY02NgIAZ4JzI7hbEh+H6q/ILpNqL6MH6KghSnblYJo6IPVEDH1DCZqTA+QlOIL9a/GCjAZc/OwKAV5SrP6ytuEAoUiBQAqSuyc9MIUbK0AjJzXcA6o+XU4jwTAoAsD2kgIRJkYVygwmEtC2RncjMwOIsFKrt7SQbrSQAFcmKAhLp5OIsDa55MPpBtKwwygscxLSPVxAzFB9lJRtrE+BQA5HQag8dxWnYAAAAASUVORK5CYII=';
            case 'properties':
                return 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAA8AAAAOBAMAAAACpFvcAAAAD1BMVEUNugAAAIDAwMD///8AAABRTlwIAAAAAXRSTlMAQObYZgAAAAFiS0dEAIgFHUgAAAAJcEhZcwAACxMAAAsTAQCanBgAAAAHdElNRQfhCw8JNx3Cr7SiAAAASElEQVQI14WKwQ3AMAwCeWSAzGB5A3uACth/psaR+i4fTnAAsOxnnwYjcmBlRGzYJAfUmTmXKHJkuTr7qKoqfVAX3PbP4hvgBRceEdhe8HcMAAAAAElFTkSuQmCC';
            case 'print':
                return 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAMAAAAoLQ9TAAAAUVBMVEVAQEDMzMyNjaHT3vGAgICnqcb///+Hqdzk5ORPUFav1v+ur6/j8v/T6f+IiInDw8SlpKJmZmbU0Mizs7P1+PPu7e3G5P+4ur6htJ/d3d293v+ew8BDAAAAAXRSTlMAQObYZgAAAAFiS0dEAIgFHUgAAAAJcEhZcwAACxMAAAsTAQCanBgAAAAHdElNRQfhCw8JOQbWSVDAAAAAfElEQVQY01WOWw7DIAwEcaMGUjdmI0Movf9BiwtSyPxYHvmxzjn/x1341biZtzEZvxuz+Bq3nde5Lx6TiAZhKFWlmnMOQcV6VCINOaiWOESl2qYqRXTBoGZYRD5dpKBoHReVLtbUDjDkEB5HkHLR7ZDrMRqPJzBFszi9/ADR0gg6ZnlGoAAAAABJRU5ErkJggg==';
            case 'preview':
                return 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAMAAAAoLQ9TAAABXFBMVEUAAAD///9rfJtwgqeImLjt9vxtfpzp9PrO1OH+/v5+jrGDk7Tx+Px9jrDs9ft+jrB3iKz8/f6Aj6+Mj5Hm8vrk8fr6/P2F0P59gIJ+gIFvf6Lt9vuY1v5tfZz6hzWX3f+Dg4Tq///L9/9/jq+97v/v/v/6/P7q9PuTrb+g2vbuuJ7ntpz0+f39s47+iDZ6iauCyPH/3n/8gzmLlJqAkLG2urygq76mp6iE0f/E2t5sfJt9f3/Qknh4h6mhqr3q9ft0dXagrMWW2v9tfp1+fn2Mj5BzeHtrfJp9goT/4XGEhYW76PiY1fJwgKNtfJzg6PDHj3rC7fmQ1f+d2f79gjRmd5vn8/qOmKCU0PCm5//o8/qrq6uoqqvr9ft4enru9Pn4/P11eXup3fTrtJeAiZGHl7fZ4vWmqKqjpaeQze/u9vz9/v6Zq7dsfZx+gILWxYz5+/2rqqpxgJ/y///5GIvjAAAAAXRSTlMAQObYZgAAAAFiS0dEAIgFHUgAAAAJcEhZcwAACxMAAAsTAQCanBgAAAAHdElNRQfhCw8JOQ1Bm4lIAAAAvUlEQVQY02NgwAQsLNzcvLwCAswcMAFGCGDm4kAIcGarFfCnQUVSQdKcgmKO/MzMYAETID86L1EyJt6/CCwgxMhYaFmsFJmTnuUHFeD0UFQN1HRSYLUCCygzCrqpeCdFBMXxMYEF9BkTJOR9Mg2CvezdwQK2jDrmGhbiMsauyXZgAV9GHp4MFwdr4Xw9G7CAFCMrqzRfrLp2iKecLkgglJGdnT0qTERUy8gwHCTA5MzGxiaby8RkFpBiiul3AHwcGwdW2rtpAAAAAElFTkSuQmCC';
            case 'paste':
                return 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAMAAAAoLQ9TAAABj1BMVEUAAAD/////v1n/tE7/sUtzc3b/rkj/uFJ1dXj/sEr/skz/xmD/rUf/wlyenJz/tU+tra38/Px1dXf/u1X/vFaIgnqioJ739/fu7vq/v+j/yWR9dWX8uG3Cwtv/uVN0dHb2pU/Fxd3/wlu9ubLynER8cGHxo06lpKGKhXzxmTzi4vX6qVT7qkWxsdD+48jBwdiamJzjgBrunUvtjy7rjCy2ttT/t1Gko5/x8fHkgh7/w126us3/r0n8/P+1tcT/xWC8t7D/vVfR0eBycnb11bWsrMHe3u//wFr/ulTKyuCQkJiXjIX5qVPu7u6lo6DvlDf538T/zml2dnjk5P/tkzP8t27n5/WhnpyKhn3r6/rExN74oj2kop/imku5udvPz+3ExNqhn530pFXPz/Hi4uLz8/vJyd6pqcHCwuy3t93nhiD/zGn/ymN0dHfRv634plGdm5voiSidnaLjgRvu7vzTxrvDw/Td3d34+P+cmZqmprTmpmTm5vyxscn0n0nt7fi9ve/zlzXS0vWcmZX9ul0S5YICAAAAAXRSTlMAQObYZgAAAAFiS0dEAIgFHUgAAAAJcEhZcwAACxMAAAsTAQCanBgAAAAHdElNRQfhCw8JOx+AFJqCAAAAyElEQVQY02NgwA58BQQs4By9UO3q0vKUvNjGpACwgExLoLKguLigA7OOGljAJztHOkJUVEOVhdMELJAvxc3NyyTMzszFUggWULC34nVv9osRC+erLAIJ1CkxMTmqMwKBbUUQSECFSUTEwzw1uSnKzokDJKApLMduJsZYLxHmVgMW8Gfn52dOLGkoy0xPAwuEAI3n5IuUzIhz9RQCCRizsLHxFGjpmtZWJYAFjKLZbHgMgmUV9a3jc0ECLlmWxYbe8qxA4OyF6W8ADfcjk3RxnlIAAAAASUVORK5CYII=';
            case 'group':
                return 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAAABmJLR0QA/wD/AP+gvaeTAAAACXBIWXMAAArwAAAK8AFCrDSYAAAAB3RJTUUH4QsPCTsm3xESigAAAWlJREFUOMut0rFqVEEUBuDvhhTxAXbtlzRiJddSUmRfIE0afYB0di52FtsEQqos4n2AVKYI1iGFEpTlLjZCRNBisyySQMhIINnq2syVuasJiA4Mw5n5zzn//Ofnv628qHAXS8hu3Qk2S5L/fo02HtZV2/Liu9HGK0xwAWVhEALdni2c4io2HETsaAEVfsSaE3zAQVkY5DnLy7x+7hmmeIeDBHu8GIPreF7Eh68hsLfXINzBe5wk2LPFuV9lZeFzpL2CFbTjd/oh6Hd7OjUW1UKaXRZ28pzzc2LyW7yM2+oqb174FuFLyBoMQmA8/hW2o3Bf6ovxmNms8Z41GHR7tobDP08sBIZD1jftp/fzGpyub3qCTlno4ylMpw1NMqxFdk0N4jQ+YTcE8pxWi8vLhia7Cfa3AhVmOOn2dA4POTri8bb92HmSjLFC9q9WfnSrlXGn9sFNVs6Smd7HAxzjLFKsDWMubuEePv4ERXmHu+21y/0AAAAASUVORK5CYII=';
            case 'emulation':
                return 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAMAAAAoLQ9TAAAABGdBTUEAALGPC/xhBQAAACBjSFJNAAB6JQAAgIMAAPn/AACA6QAAdTAAAOpgAAA6mAAAF2+SX8VGAAACqVBMVEUAAAAAAAAbhLIXgrA5lr2Ry9////8AAAAag7J2udE3lLsAZJw3k7tLocRUp8k3k7sAZJw3lLt2udAAAABcp8hDmr8wjrdQpMZfs9NfuNpbutxfs9NQpMYwj7dDmr9cqMgAAAAZgLAxj7goibRSp8lYtthLt95Ft+BEt+FFt+BGt+BLt95YtthSp8koibQxj7gZgLAYf68Aa6BAnMJNsNVGtt9GuOFGuOFGtt9NsdVAnMEAa6AYf68ujrgri7ZDpctGtNxHuOFHuOFGtNxDpcsri7YujrhKqswtkLpAqtFGtt9Gtt9AqtEtkLpKqszY//8rkbtAq9NFtuBFtuDA//8pjro/qdJEtd9Etd8/qdIpjrrA//84nMIliLU6o8tCstxCstw7o8sliLU4nMIega8bfq0yl8E/rNZBs95Bs94/rNYyl8Ecfq0ega8RdKgAX5kkh7Q2ncc+rNc/sN0/sN02ncckh7QAX5kRdKgTdqkZeqsUdqgnibU0msQ7ptE8q9c9rNk9rdk9q9c7ptE0msQnibUUdqgAAAAggq8ae6sTc6UhgK4sjbgymMI1ncc1nccymMIsjrghga8Tc6YafKsAAABZ//8bj8oAFCQNRWIVWHgZZIcaZIcVWXoOSWgAGi4XeKoqsPEAAAAFIC4AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAUg8EAAABRt9xNttxFuOFGuOFFt+GIyeGv0d1Yt9lFt+FHuOFGuOFDt+CS0un+/fzI3ORouthGt+BGuOFGt+FDtuCR0Oj////////f6OtzvtlFt+FEtuFCteCQ0Oj////////R7fdmweNDtuFDteBAtN+R0Oj8/v624vNXveNBteBDteA/s99+yeWZ1u5Jt+BAs99CtN9Fs99Bst8/sd5Asd7////kFFuTAAAArnRSTlMAAAAAAAAAAAAAAAU4d544BQAAAAAAIZns///smiEAAAAAAB67//////////+8HgAAAAaZ////////mQYAADjt/////+04AAB1/////3UAAJb///8Alf////+VAABy/////3IAADTq/////+o0AAAEkf//////kQQAAAAZsf//////////sRkAAAAZj+n/////6Y4ZAAAAAAxQlrW0lEwJAAAAAAQTJjEwJBEDAAC90wIBAAAAAWJLR0TixgGeHAAAAAlwSFlzAAALEwAACxMBAJqcGAAAAAd0SU1FB98CGBIdHUMZX5EAAAEbSURBVBjTARAB7/4AAAABAgMEBQYGBQQDAgcAAAAACAkKCwwNDg4NDxAREggAABMUFRYXGBkaGhkbHB0eHyAAISIjJCUmJygpKissLS4vMAAxMjM0NTaur7CxNzg5Ojs8AD0+P0BBsrO0tba3QkNERUYAR0hJSri5uru8vb6/S0xNTgBPUFFSwMHCw8TFxsdTUVBPAFRVVlfIycrLzM3Oz1hZWlsAXF1eX9DR0tPU1dbXYGFiYwBkZWZnaNjZ2tvc3Wlqa2xtAG5vcHFyc97f4OF0cnV2d3gAeXp7fH1+f4CBgoOEhYZ6eQCHiImKi4yNjo+QkZKTlIgAAACVlpeYmZqbnJ2en6ChAAAAAACio6SlpqeoqaqrrK0AACfEZpl8ZSOkAAAAJXRFWHRkYXRlOmNyZWF0ZQAyMDE3LTExLTEyVDExOjA0OjMxKzA4OjAwCR2IgAAAACV0RVh0ZGF0ZTptb2RpZnkAMjAxNS0wMi0yNFQxODoyOToyOSswODowMEVoAFMAAABNdEVYdHNvZnR3YXJlAEltYWdlTWFnaWNrIDcuMC4xLTYgUTE2IHg4Nl82NCAyMDE2LTA5LTE3IGh0dHA6Ly93d3cuaW1hZ2VtYWdpY2sub3Jn3dmlTgAAABh0RVh0VGh1bWI6OkRvY3VtZW50OjpQYWdlcwAxp/+7LwAAABd0RVh0VGh1bWI6OkltYWdlOjpIZWlnaHQANDiHYIctAAAAFnRFWHRUaHVtYjo6SW1hZ2U6OldpZHRoADQ4f89HoAAAABl0RVh0VGh1bWI6Ok1pbWV0eXBlAGltYWdlL3BuZz+yVk4AAAAXdEVYdFRodW1iOjpNVGltZQAxNDI0NzczNzY5Dco+agAAABJ0RVh0VGh1bWI6OlNpemUANS4xOEtC8gYvLAAAAF90RVh0VGh1bWI6OlVSSQBmaWxlOi8vL2hvbWUvd3d3cm9vdC9zaXRlL3d3dy5lYXN5aWNvbi5uZXQvY2RuLWltZy5lYXN5aWNvbi5jbi9zcmMvMTE4NDgvMTE4NDg4NS5wbmduAExIAAAAAElFTkSuQmCC';
            case 'delete':
                return 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAMAAAAoLQ9TAAAA6lBMVEX//////wD/AP//AAAA//8A/wAAAP8AAABAOEApIyxXUVpCPkhxb3pfYW9+gpGRlqY5OjxISUu1v9G9ydyrtcZaY3CRqMeju9yhuNiGmbKyyejB1e9ncX5BREhqfJJneI2vxN9/j6K4zupkb31DSlPD1++zxNhUW2TE1OeuvM2Qm6lJTlQ+Qkd2fYZVYW+mu9JseIXE2O+ktcjG2e++0OSsu8ybp7V1fohUWmF/h5Btc3oQGCAgKDA/SlUwOEBsc3p3foVCRUhaXmJfcYBJW2icq7WToak4Ojt2g4gyPDlAQEAwMDAgICAQEBBc0zm1AAAAAXRSTlMAQObYZgAAAAFiS0dEAIgFHUgAAAAJcEhZcwAACxMAAAsTAQCanBgAAAAHdElNRQfhCw8JOy7Rypq4AAAAg0lEQVQY02NgwACuPK5oAl6CIlCmmRuYtLLz4ofwuXTBtKm9l5cDkDbw8jCFKtXy8rJk4PMyN4ObY+RkJyCgp4VksqI3hyM/slW23t523Mh8G14zdW9FOF/HxwJoqYy7G5QvyikLdoa2MoQvaW0jB2ZYekqCaWdfFahSeUsI7cKADwAAXnAQdtLQf9IAAAAASUVORK5CYII=';
            case 'cut':
                return 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAMAAAAoLQ9TAAABCFBMVEUAAAD///8oZ7x5eXmop6d8nNGrq6p4eHikpKNXpOYcY7khW7PD0epnlNVomNOnpqZ5vPJKd7wfXbVRmd52dnV4t/Dc3NwbXLZRktlwmreVk5AJTK/d6PdqZF9ycXClpaRPl950dHO/ydDp7vhCldaIwfi+z+pDbLWw0/S8vLxrjslwrehgmcgsf9H6+/0mZry80ert9P2npqWXsNnd5PR+ufNMesR1dHSvr67W1tZJmNssfswgXrqhoJ/t7e1QdruQj49fnONZgsZ2uO6qqqiop6YbZbubmpkkWbHw8PD2+fywr680fcozZLr4+PinpqSlpKMZWLI+hc0oV638/P7CwsGHvet3d3eqQy88AAAAAXRSTlMAQObYZgAAAAFiS0dEAIgFHUgAAAAJcEhZcwAACxMAAAsTAQCanBgAAAAHdElNRQfhCw8JOzbCpgLuAAAAg0lEQVQY02NgIBJYaKIJGImFogqwMDKjCnAwhqMK2HqaMzAYyuiB2O4u8v7e/JZySqoSXPpqQAFXO0ZGvwAp2TAfewMrbpASNkURdgdJHRVxYy9naZgZGqJC1kFuwgrqMAFTDx4mPlYnrRCYgKMZg7YNsrW8ugKcvsgCyoKBwSbYvQkAuWUO+Hqdt9MAAAAASUVORK5CYII=';
            case 'copy':
                return 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAMAAAAoLQ9TAAABZVBMVEXCAhj///9wgqdxW6Lm5uZrfJtoVpfOxt+vvduImLj+/v59jrB+aaq8sdWGlbXp9PqFcq9tfpx8aKm5wtt+jrGAj6+9w89qepnQ0OS8xNvNzOO9xNvQz+PDx9ujlMH6/P5tfp24wtu7w9vOz+PCwuLn8/qtvNuluNqnmM54h6nk8fprfJrk5ea0wNv7/P5kU5bq9fvm8/rm8vrKx9t/jq/4+/2AbazZ4vXt9vyikcvV1OSyv9utvt63wdvo8/pneZd2Yqa6r9WPgbB3iKy0wd6qutttfZyqu9pre5nAxt7h4ea5rtSDk7T9/v77/f7Mzt/2+v24qtTCx9vi4uba7fikksy0v9ufnMNwgKO+xduzv9u6xN6llMzAxdxwgJ9nVZZsfJtoe5nf3uVsfZyoutpmd5vo9PtjUpPW1eVrV5jGyt79/f7S0uSputvs9fvz+fx6iavg6PChkMtpVZbb2+TKweBuW5q3XiEQAAAAAXRSTlMAQObYZgAAAAFiS0dEAIgFHUgAAAAJcEhZcwAACxMAAAsTAQCanBgAAAAHdElNRQfhCw8KAALd5qc6AAAAxklEQVQY02NgwAoEBMyEhByY2eECciwgwMwDFuHk9OHmdmYS0/FiLgWLcDKCAJOIWDAzEAAF+BgZuXzlmcxFwgM1jMsgAoxcfkxAkFSSkQkUEGX0z/KMdrEpjOONsWIDC3Bl65kG5Fsk8IbmgARMGGOlhcM4XPNYHS0l4oECBYyykcK6HLkG2t5FysVAAU3GIElba7WUNA8ZKZV0oEAEo5JiFIe7uqG4E5s+yIxURn5+O1UjrRB7qD9YFQQF3ZJZWRNZsXscAIlfHNxcQoFAAAAAAElFTkSuQmCC';
            case 'connector':
                return 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAMAAAAoLQ9TAAADAFBMVEX///8AAFUAAKoAAP8AJAAAJFUAJKoAJP8ASQAASVUASaoASf8AbQAAbVUAbaoAbf8AkgAAklUAkqoAkv8AtgAAtlUAtqoAtv8A2wAA21UA26oA2/8A/wAA/1UA/6oA//8kAAAkAFUkAKokAP8kJAAkJFUkJKokJP8kSQAkSVUkSaokSf8kbQAkbVUkbaokbf8kkgAkklUkkqokkv8ktgAktlUktqoktv8k2wAk21Uk26ok2/8k/wAk/1Uk/6ok//9JAABJAFVJAKpJAP9JJABJJFVJJKpJJP9JSQBJSVVJSapJSf9JbQBJbVVJbapJbf9JkgBJklVJkqpJkv9JtgBJtlVJtqpJtv9J2wBJ21VJ26pJ2/9J/wBJ/1VJ/6pJ//9tAABtAFVtAKptAP9tJABtJFVtJKptJP9tSQBtSVVtSaptSf9tbQBtbVVtbaptbf9tkgBtklVtkqptkv9ttgBttlVttqpttv9t2wBt21Vt26pt2/9t/wBt/1Vt/6pt//+SAACSAFWSAKqSAP+SJACSJFWSJKqSJP+SSQCSSVWSSaqSSf+SbQCSbVWSbaqSbf+SkgCSklWSkqqSkv+StgCStlWStqqStv+S2wCS21WS26qS2/+S/wCS/1WS/6qS//+2AAC2AFW2AKq2AP+2JAC2JFW2JKq2JP+2SQC2SVW2Saq2Sf+2bQC2bVW2baq2bf+2kgC2klW2kqq2kv+2tgC2tlW2tqq2tv+22wC221W226q22/+2/wC2/1W2/6q2///bAADbAFXbAKrbAP/bJADbJFXbJKrbJP/bSQDbSVXbSarbSf/bbQDbbVXbbarbbf/bkgDbklXbkqrbkv/btgDbtlXbtqrbtv/b2wDb21Xb26rb2//b/wDb/1Xb/6rb////AAD/AFX/AKr/AP//JAD/JFX/JKr/JP//SQD/SVX/Sar/Sf//bQD/bVX/bar/bf//kgD/klX/kqr/kv//tgD/tlX/tqr/tv//2wD/21X/26r/2////wD//1X//6r///9SiC1HAAAAAXRSTlMAQObYZgAAAAFiS0dEAIgFHUgAAAAJcEhZcwAACxMAAAsTAQCanBgAAAAHdElNRQfhCw8KAAukOh+eAAAAgElEQVQY011PsRHEMAijiQpGUZHRUpBN8Db5Td7DOAaTy11U2ZIQSCRghNLkgYHWzkkWRfWRcGXq2keha3i0xZv8hwciB1NUpJGH0MqdjFFwDgOg2IIxCCyIqaOIfY703+WqETtHKhTbEyrwtTb+Ddnjc5jsn9NXOfO33KqPqn8D0uBwagvr7V4AAAAASUVORK5CYII=';
            case 'baseInfo':
                return 'data:image/png;base64,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';
            case 'aligntop':
                return 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABMAAAATCAMAAABFjsb+AAAAulBMVEUAeR3s9/+Tyf+DwP+f0P+p1/90tv9krf//4gD//cDW6f8hctT/3wAldNT/+wL/8AD/2gD/zgD//9P/wgAodtT/0QD//8MtedQGXcnn9P//3AD/8wD/6AD//xn/1gD/wADd7f//2QD/1AD/+QDZ6///yAAqeNT6sQD/+sD//9Agbcz//8ng8P8ZbdT//8H/ywDk8v//+cD//97/6gAdcNTq9f//5QD/+8D/7gAWbNRZpv8bbNC4aSBMRGWYCGDpAAAAAXRSTlMAQObYZgAAAAFiS0dEAIgFHUgAAAAJcEhZcwAACxMAAAsTAQCanBgAAAAHdElNRQfhCw8KABVeNSL9AAAAgklEQVQY02NgoADYYgKsYtiADQwwWEOBFoONkZC2Hqe5IVCMEQLEGWw0ZfmkjTkUgWKmrGCgxmAjpswvwyMnCBSTZAEDEQYbTgszKSV9YaCYARMY8DLYaHAIiKrKqwPFdJjBgBvZXgU2MDBBcpW1CjsY6CKLcVmBgSUhMUsIkCA2+ACurh9i5gjR/wAAAABJRU5ErkJggg==';
            case 'alignright':
                return 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABMAAAATCAMAAABFjsb+AAAAulBMVEUAWQfs9/+Tyf+DwP+f0P+p1/90tv9krf//4gD//cDW6f8hctT/3wAldNT/+wL/8AD/2gD/zgD//9P/wgAodtT/0QD//8MtedQGXcnn9P//3AD/8wD/6AD//xn/1gD/wADd7f//2QD/1AD/+QDZ6///yAAqeNT6sQD/+sD//9Agbcz//8ng8P8ZbdT//8H/ywDk8v//+cD//97/6gAdcNTq9f//5QD/+8D/7gAWbNRZpv8bbNC4aSBMRGX8i8zvAAAAAXRSTlMAQObYZgAAAAFiS0dEAIgFHUgAAAAJcEhZcwAACxMAAAsTAQCanBgAAAAHdElNRQfhCw8KACLmiIfyAAAAbUlEQVQY02NgQAW2tjCWDQwgixkJaetxmhuiiGnK8kkbcyiiiIkp88vwyAmiiHFamEkp6QujiGlwCIiqyqvbELAX033WCIAQYzSVNNBRUOHiskQSY2VhYmZjt7Kir5iWuJoIL7eJrqWlBE5/AAAeSB9ioU53WQAAAABJRU5ErkJggg==';
            case 'alignmiddle':
                return 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABMAAAATCAMAAABFjsb+AAAAn1BMVEUAFgAWbNTs9/+p1/90tv+DwP9krf+Tyf+f0P//4gDW6f//zgD//9AqeNQZbdT//cD/2QDq9f//+sDg8P8gbcwodtT//9P//8P/+wL/ywD/8AAGXcn/wgD/+QD//8ktedT6sQAhctT/1gD/+cDd7f///97/5QD/7gD/wADZ6////xkdcNTk8v8ldNTn9P//2gD/+8BZpv9MRGUbbNC4aSANKa+uAAAAAXRSTlMAQObYZgAAAAFiS0dEAIgFHUgAAAAJcEhZcwAACxMAAAsTAQCanBgAAAAHdElNRQfhCw8KACroUw/AAAAAfElEQVQY02NgoAowBgMRVDEmEJAHs00ggMFYkBkIeCFiqmJyBspAMT0OIBCFiPFoSXAKAMV02IFAFyhiZGQiLiulxG1iZCzMCgSKRkYgMX51NUkZoJgKCxBoA8WAeoU49TUUgHo12YCAD9VeLkMgYER1HzYxRhCQZqALAACobhCpT6mwVwAAAABJRU5ErkJggg==';
            case 'alignleft':
                return 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABMAAAATCAMAAABFjsb+AAAAulBMVEUAUVvs9/+Tyf+DwP+f0P+p1/90tv9krf//4gD//cDW6f8hctT/3wAldNT/+wL/8AD/2gD/zgD//9P/wgAodtT/0QD//8MtedQGXcnn9P//3AD/8wD/6AD//xn/1gD/wADd7f//2QD/1AD/+QDZ6///yAAqeNT6sQD/+sD//9Agbcz//8ng8P8ZbdT//8H/ywDk8v//+cD//97/6gAdcNTq9f//5QD/+8D/7gAWbNRZpv8bbNC4aSBMRGWSBgn3AAAAAXRSTlMAQObYZgAAAAFiS0dEAIgFHUgAAAAJcEhZcwAACxMAAAsTAQCanBgAAAAHdElNRQfhCw8KADAVMfa6AAAAcElEQVQY02NgYGCwtWXAAEAxGxhAFjMS0tbjNDdEEdOU5ZM25lBEERNT5pfhkRNEEeO0MJNS0hdGEdPgEBBVlVe3IWAvNvdZIwBCjNFU0kBHQYWLyxJJjJWFiZmN3cqKvmJa4moivNwmupaWEtj9AQBdcR9iA7HYOQAAAABJRU5ErkJggg==';
            case 'aligncenter':
                return 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABMAAAATCAMAAABFjsb+AAAAnFBMVEUEAADs9/+p1/+DwP+f0P+Tyf90tv/Z6//W6f8ZbdT/4gAqeNQtedT/7gDd7f//+wIhctTq9f///94gbcz6sQD/+cD/5QD//8n//9P/+8D/8wD/2gD/ywD/+sD//9D/2QDn9P///8H/0QDk8v8GXcn//cD/3AD/wgDg8P///xkldNQodtQdcNT/wAAWbNRZpv9MRGVkrf8bbNC4aSBl5tlvAAAAAXRSTlMAQObYZgAAAAFiS0dEAIgFHUgAAAAJcEhZcwAACxMAAAsTAQCanBgAAAAHdElNRQfhCw8KADgb6n6IAAAAZ0lEQVQY02NggAIDAwYMgCFmDAUoYkIS4oqSoqhicpr8UlzyqGKqvGJqMuqoYrJc0kq6IsaE7MB0ixESgIsxCiooa/Cxs3Nw6CHEmFhYmdkMDfX1aSYmzMOtrSWgw8mpp6eCP/xQxQCloxPfPkoAuAAAAABJRU5ErkJggg==';
            case 'alignbottom':
                return 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABMAAAATCAMAAABFjsb+AAAAulBMVEX/WIHs9/+Tyf+DwP+f0P+p1/90tv9krf//4gD//cDW6f8hctT/3wAldNT/+wL/8AD/2gD/zgD//9P/wgAodtT/0QD//8MtedQGXcnn9P//3AD/8wD/6AD//xn/1gD/wADd7f//2QD/1AD/+QDZ6///yAAqeNT6sQD/+sD//9Agbcz//8ng8P8ZbdT//8H/ywDk8v//+cD//97/6gAdcNTq9f//5QD/+8D/7gAWbNRZpv8bbNC4aSBMRGUL4yqbAAAAAXRSTlMAQObYZgAAAAFiS0dEAIgFHUgAAAAJcEhZcwAACxMAAAsTAQCanBgAAAAHdElNRQfhCw8JMxNBe1yhAAAAg0lEQVQY02NgoAqwhgItZDFGCBBHFjNlBQM1ZDFJFjAQYbCBAQZrAyYw4GWwMRLS1uM0NwSK6TCDATeDjaYsn7QxhyJQTIENDEwYbMSU+WV45ASBYirsYKDLYMNpYSalpC8MFOOyAgNLBhsNDgFRVXl1FDGEvZYQIIHVl7aYAKsYkQAAEZgfYn5a0J4AAAAASUVORK5CYII=';
            case 'stageBg':
                return "url('data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iNDAiIGhlaWdodD0iNDAiIHhtbG5zPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwL3N2ZyI+PGRlZnM+PHBhdHRlcm4gaWQ9ImdyaWQiIHdpZHRoPSI0MCIgaGVpZ2h0PSI0MCIgcGF0dGVyblVuaXRzPSJ1c2VyU3BhY2VPblVzZSI+PHBhdGggZD0iTSAwIDEwIEwgNDAgMTAgTSAxMCAwIEwgMTAgNDAgTSAwIDIwIEwgNDAgMjAgTSAyMCAwIEwgMjAgNDAgTSAwIDMwIEwgNDAgMzAgTSAzMCAwIEwgMzAgNDAiIGZpbGw9Im5vbmUiIHN0cm9rZT0iI2UwZTBlMCIgb3BhY2l0eT0iMC4yIiBzdHJva2Utd2lkdGg9IjEiLz48cGF0aCBkPSJNIDQwIDAgTCAwIDAgMCA0MCIgZmlsbD0ibm9uZSIgc3Ryb2tlPSIjZTBlMGUwIiBzdHJva2Utd2lkdGg9IjEiLz48L3BhdHRlcm4+PC9kZWZzPjxyZWN0IHdpZHRoPSIxMDAlIiBoZWlnaHQ9IjEwMCUiIGZpbGw9InVybCgjZ3JpZCkiLz48L3N2Zz4=')";
            case 'duplication':
                return 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAMAAAAoLQ9TAAAABGdBTUEAALGPC/xhBQAAACBjSFJNAAB6JgAAgIQAAPoAAACA6AAAdTAAAOpgAAA6mAAAF3CculE8AAAC+lBMVEUAAACF2v+E2v+I2/+W3/+e4f+b4f+U3v+Q3f+V3/+f4v+k4/+m5P+k5P+E2f+N3P+L2/+g4v+f4v+T3v+H2/+A2P9/2P9/2P+H2v+Y3/+e4v+a4P981v921f+D2v9bz/+R3f+O3f+N3P+F2v9+2P9+2P9+2P991/+G2v+V3v+T3v911f9z0/9/1/+E2P6A2P971v961v961v961v951v971/+G2v+K2/+F2f931/981/991/931f901P901f970/lz1P921f971v951v9w0/9y1P9w0/9v0/9v0/940vpv0/9v0/9w0/9q0f9q0f9q0f9p0P5o0f9q0f9p0P1kzv1l0P9kz/9l0P9kzfxey/tgzv9gzv9eyvtXxvhbzP9Yy/9bzP9bzP9WxfdQv/FWy/9Tyv9Wyv9Wyv9Qv/FItORQyP5OyP9Qyf9QyP5ItOU9m8VKwfdJx/+m4/9Lx/9Lx/9Kwfc9m8YAAABFsOFGxP111P+35/2z5PtJxv9Gxf9Gxf9IxP1FsOEAAABBncc1fJ1GtOZEwvtDxP9Exf9Exf9Exf9Exf9CxP9CxP9DxP9GwvpGsuQ0d5VAmsKf//9QvvAyc5FEpdJGsOBGsOFGsOFGsOFGsOFGsOFGsOFGr99DocwtZHxNs+KE//9+2P961v961v951v901f901P901f9z1P6D0/Zv0/9u0v5v0/9t0v6K1fbi8ffF5vVozvtr0P2G1fdtz/qG0/bj8vj////5/P6V3PuK1vjA4/KR2fnB6/3C5PPh8fj////7/v+v5v9q0f9lz/9ezf+w5v+76v9x0//G7v/////////4/f+s5f9jz/9fzv9gzv+s5f+W3v9Xy//G7f/////////W7vllzftazP9bzP+q5f+U3v9Tyv+q5f/b9P/Z8//Q7/yG1PdVyv5Wyv+n5P+P3P9Lx/9Tyv9Wy/9ezP1+0fZay/5Qyf9Qyf+v4PdxyvJyy/JxyvKL0/PJ7PxUyv9Lx/+z5Puz5Pu35/yX3v////8a8X0YAAAAo3RSTlMAAAAAAAx53vzrlxwAAAAAAAgMhfr/////rBEAAAAAAFi7wvT//////FkAAAAAHNj////////LOwAAP7L7///////ZNz7f////////qK7//////97f////5OT//+Te/////9u+/////7qD/v///oA14//////jNAOI//////////+EAwAXqvz///////////mfFAAAABiBwsjIyMjIyLpuEgAAn4bmzwAAAAFiS0dE/UsJk+kAAAAHdElNRQfdCQ0VLAC+0hXIAAABG0lEQVQY0wEQAe/+AAAAAQIDBAUGBwgJCgsMDQAAAA4PEBESExQVFhcYGRobHAAdHh8gISIjJCWjJicoKSorACwtLi8wMTIzpKWmNDU2NzgAOTo7PD0+p6ipqqs/QEFCQwBERUZHSKytrq+wsbJJSktMAE1OT1CztLW2t7i5urtRUlMAVFVWvL2+v8DBwsPExcZXWABZWsfIycrLzM3Oz9DR0ltcAF1eX9PU1dbX2Nna29xgYWIAY2Rl3d7f4OHi4+Tl5mZnaABpamvn6Onq6+zt7u/wbG1uAG9wcXLx8vP09fb3+HN0dXYAd3h5ent8+fr7/H1+f4CBggCDhIWGh4iJiouMjY6PkJGSAJOUlZaXmJmam5ydnp+goaJzUXyH4ydBvgAAACV0RVh0ZGF0ZTpjcmVhdGUAMjAxNy0xMS0xMlQxMDoxODo0NyswODowMGYsUmgAAAAldEVYdGRhdGU6bW9kaWZ5ADIwMTMtMDktMTNUMjE6NDQ6MDArMDg6MDACD05gAAAATXRFWHRzb2Z0d2FyZQBJbWFnZU1hZ2ljayA3LjAuMS02IFExNiB4ODZfNjQgMjAxNi0wOS0xNyBodHRwOi8vd3d3LmltYWdlbWFnaWNrLm9yZ93ZpU4AAAAYdEVYdFRodW1iOjpEb2N1bWVudDo6UGFnZXMAMaf/uy8AAAAXdEVYdFRodW1iOjpJbWFnZTo6SGVpZ2h0ADk21XbUZwAAABZ0RVh0VGh1bWI6OkltYWdlOjpXaWR0aAA5Ni3ZFOoAAAAZdEVYdFRodW1iOjpNaW1ldHlwZQBpbWFnZS9wbmc/slZOAAAAF3RFWHRUaHVtYjo6TVRpbWUAMTM3OTA3OTg0MHKSccAAAAASdEVYdFRodW1iOjpTaXplADQuNzFLQhPgmNoAAABfdEVYdFRodW1iOjpVUkkAZmlsZTovLy9ob21lL3d3d3Jvb3Qvc2l0ZS93d3cuZWFzeWljb24ubmV0L2Nkbi1pbWcuZWFzeWljb24uY24vc3JjLzExMjczLzExMjczNTUucG5nN59hfwAAAABJRU5ErkJggg==';
            default:
                return '';
        }
    };

    /**
     *
     * @param {dom} frame iFrame的dom
     * @param {number} scale 相对窗体的比例
     */
    var openFrameWnd = function (frame, wndTitle, scale) {
        if (frame) {
            var w = document.body.clientWidth;
            var h = (document.body.clientHeight || document.documentElement.clientHeight);
            var constScaleWnd = scale || 0.6;
            frame.style.width = (w * constScaleWnd) + 'px';
            frame.style.height = (h * constScaleWnd-27) + 'px';
            var wndW = w * constScaleWnd;
            var wndH = h * constScaleWnd;
            var topW = 0;//(w - 100 - wndW) / 2;
            var topH = 0;//(h - 100 - wndH) / 3 < 60 ? 60 : (h - 100 - wndH) / 3;
            wndProp = new mxWindow(wndTitle, frame, topW, topH, wndW, wndH);
            wndProp.setClosable(true);
            wndProp.setMaximizable(false);
            wndProp.setMinimizable(false);
            wndProp.setVisible(true);
            wndProp.setResizable(false);
            return;
        }
    };

    var closeFrameWnd = function () {
        if(wndProp){
            wndProp.destroy ();
        }
    };

    //获取画布编辑器：editor
    var getGraphEditor = function () {
        return graphEditor;
    };

    //设置画布编辑器：editor
    var setGraphEditor = function (graphStage) {
        graphEditor = graphStage;
    };

    //读取xml格式进行图画渲染
    var renderModelFromXml = function (graph,xml) {
        if(graph){
            var model = graph.getModel();
            // 渲染画图区域
            model.beginUpdate();
            try {
                if(xml){
                    var xmlDocument = mxUtils.parseXml(xml);
                    if (xmlDocument && xmlDocument.documentElement) {
                        var decoder = new mxCodec(xmlDocument);
                        var nodes = xmlDocument.documentElement;

                        var model = graph.getModel();
                        decoder.decode(nodes, model);
                    }
                }
            } finally {
                model.endUpdate();
            }
        }

    };

    //获取UUID
    var getUUID = function (count) {
        if (uuidArr && uuidArr.length > 0) {
            return uuidArr.shift();
        } else {
            initUUIDs(count);
            return uuidArr.shift();
        }
    };

    //初始化UUID缓存数组
    var initUUIDs = function (count) {

        if(!count){
            count = 100;
        }

        $.ajax({
            type: 'get',
            url: hostUrl + "generator/getGuuids?count="+ count + "&_t=" + new Date().getTime(),
            async: false,
            success: function (data) {
                if (data.success) {
                    uuidArr = data.result;
                }
            }
        });
    };

    //根据节点id更新节点属性
    var updateCellAttr = function (cellId,attrName,attrValue) {
        var model = getGraphEditor().model;
        var cell = model.getCell(cellId);
        model.beginUpdate();
        try {
            var edit = new mxCellAttributeChange(cell, attrName, attrValue);
            model.execute(edit);
        }
        finally {
            model.endUpdate();
        }
    };

    //根据节点ID获取节点信息
    var getCellById = function (cellId) {
        var model = getGraphEditor().model;
        var cell = model.getCell(cellId);
        return cell;
    };

    return {
        getUrlParam: getUrlParam,
        openFrameWnd: openFrameWnd,
        closeFrameWnd:closeFrameWnd,
        cellsToString: cellsToString,
        getBaseImgByBtnType: getBaseImgByBtnType,
        setGraphEditor: setGraphEditor,
        getGraphEditor: getGraphEditor,
        renderModelFromXml:renderModelFromXml,
        getUUID: getUUID,
        updateCellAttr:updateCellAttr,
        getCellById:getCellById
    };
});

/**
 * base64的组件图标，暂时无用
 case 'person':
 return 'data:image/png;base64,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';
 case 'start':
 return 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABYAAAAWCAYAAADEtGw7AAAAAXNSR0IArs4c6QAAAx9JREFUOBGVVU1PU0EUnembfkEfpCiFUAOaEEOERJKGla78A3Xjzn/gipXhD0hcuXJv4tYN/gA31o2EBCJGTQwKkUYIHymF0o/3Op4zvHnwnmjDJcOduXPumTN3pvOk+IdprfOYKrN1tZ6BHwug1YSU6+gvsUkpD4N4xMnICAMQZuDmQbYAAnevfeJ9rv1WB+2GgQ6l+sT04Kh3PdWvgKkDs4iJF1igaQDBvwgxSIta6CUpZOnjwZZ+82tN/mhQkL6YY/q3+obEoxt39dzQuETOCnLKIN+2wJCYpFCw3NF+4eX3D85arWroCLC0tm+TQChmB4viyeQ9PymdXaifs+QGw+0DVGl3/dnnX985281asHCMSmMJiRg9LZguZgfF06kHfiqhVhG6z7IkzhBintt/9XPZOeg0RCaRNC3rqNCzn1VJEXr0iWNsH/VnLkhL4Js3a0JtHm3zS30n93oLpaIYKoEhDoEIWYVWYgCw4ulpj8dL+o47coycCYUxi+6+39uAAg6vYmQ8V1LZ25DTA6MugmVDXPdaXq3TVFknFQGeJwU1ja1pKe0sOAS5XJUuKxzaTPW0BtJkuLYFxgPxrcfWMWdKrtvu8IxC8lir6+FQLiG+LNOyc872gyKzMOQipymqkgmRRn3tQdHT4ofXcx455KKRrZpT6Ukq7mVxYrN3q5rJ0IP6cidVhf56IePeBPFVrwSY4scnxXDG9cD5iWRLuGYPh9M5cYxTDesWKAhzOY7dYxO68A8755Ul51uJOuax7uZhu5H7Vt/Bg3KWTvxfv2Bs29b9bD76A5oaGNH5VN8xFE8o1O0Q4EU8h88KaVcceVBt7xsQEcV21WDelpd+QGUEOJixSE52qIJvcAWv2+zmyb7T6voM99p5uKW0dMRE/zUfr9sqsswjZIjJAfIi3DJ8YbdVd079DsP/NTxcIoPHCTv1oXIX4OizabMDcn5ySrjoGocpfRSafzSqsNXgfe1PZjTuP8MraJGHHuOosSxoC2hHaLqru52239FNr20a+4xxDkYMsSxlxMJSRKIYAJyHMx9T+MjHFOOeH9M/Wm+RpJg0bU0AAAAASUVORK5CYII=';
 case 'over':
 return 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABYAAAAWCAYAAADEtGw7AAAAAXNSR0IArs4c6QAAAyVJREFUOBGFVT1vE0EQnd2z4xjH5MORQXaRIIhAYIlIEVTUdMg0SHQU1BQp8wdwSUGNEB0SDYGOEqUiihSEQQIFoRRxYQFx4hjinH3Dm91b++7ikLH2djzz9s3s7OydohOEmafhqprBQYWYSgaqqEFK16GvylBK7Rp74qES/wmE47AtEwcrIMhz62cv2Pqc4r3fBqomZ0hfutZTU7MpYNrA1OB4ggCHUa4YMUjLYF4lpZaC+gf2371S3PhByPaYqPIFSt++x7pyU2HNBtZUQb7jgANiSxqsk+8X/ZdPveDrR+AJePCGxE6XWUTs3pXrlL7/qE/pdBPZ33DkBmK2z7xG/tFi91nNo6YLLG5htUwMXeEns4jz6mKZxh6ugHxsE5nckrJog5CaYvv+6+eeklqOocwyMpEZus5kjU1mowMjM7d+kawVDvAtm6DIdhp72g6+f5novX1hUwujwQeslDCsxSBHC0uWJnXnAeuLVw+waC4FjiqUfH/jvc0yJDVRwzLI5q24zQ/jDzUicCh0TB7YqiXutHvUbqWUbPU/kqxxGHfYNe0WkXDl8iBmrgTNnZSpp2zZ7W9EACmLtIKZR/jFJ1x6/nJFMi7R0SEpHM4pvKOoYjZTqK65JyUhJtZIHCdsHPjv6pYM5DYjs0jSz7Arz1CSPBsqN3FJSXtBHGlUd7bjpbB9PSwyujwnZ0eNFApW14Vz85zJ2lBiPkGOHR5wrl/cEl0o9sD5SchWKZO9SzNFnOi+84+cB5mP9MKYO4sLZBJ8o9wFwe2Z4K06/tvGkLVOl1nEXRbXFckLpBcqrKYK9oIAtAtATU3PPg4K50m197A9y+TeCzJbidc0VvP8FIFDgDXhNCtALCe3RkGwGGx/86jbNTzilBCO1hgjD+djeY/MLfRJ6024zUtosAbkZRjXQV4Mmg2P/nQiFKNV037ZHOliSUibQMVfm25ZSC6fnCU6/Mu831LU822xBRRt5HSaVH6SafyMJLeBEXvRCzwmUhaMFYx9DOZ+32cJ0ulw0Dlgo4vNimAEay9BhGlQiojNqAAPP6ZEFRjtxxTND/3Uj+k/MeF7iJW0kFwAAAAASUVORK5CYII=';
 case 'dispatch':
 return 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAFAAAABPCAMAAABLT7LRAAAABGdBTUEAAK/INwWK6QAAABl0RVh0U29mdHdhcmUAQWRvYmUgSW1hZ2VSZWFkeXHJZTwAAAMAUExURaissxESErK2uwQEBCUmJo+SlzY2No6OjvL094qKiqKnqy4uLqGhobCwsa2usKmqrEFBQMjIyKersVpaWhQUFKuvtXFxcZmZmbq7vTIyMq6yuPb6/ayxtEpKSqessg0ODh4eHjg4OCkpKby9vre4urKztRgYGJ6ip4KCgnt7ez09PVVVVYeKjwcHByoqKq2ytauvs6ytryIiIkVFRSkqLCAgIJygpTQ0NBYWFicnKCIiIRYXGBkaHA8QER8gIhsdHR8fHxgaGxsbGxwcHiEiIxITFBcYGR4fIPb29nNzc/7+/tjY2P7//8TExNbW1kNDQ93d3To6OsbGxra2tomMkfv7+9TU1Pj4+Dw8O/7+/zg3N/P1+Pz8/NfX1zs7O8PDw/39/dLS0oiMkcXFxefn52JiYq6uromNktPT0/n8/9vb276+voqNkrS0tEJCQry8vPv///P2+ePj4/j6/fv+/7S1tsrKyvHx8fT09MLCwtzc3PP2+hQUE3d3dzo6Ofj7/vj8//b4+7K0tfz//7Ozs9bW1ygqK7e3t/f6/picobi4urW1tcfHx3h4eJOTk/r9/8vLy/L1+Pb5/ImNkf3//+Dg4GhoaD8/P+/v72pqavn5+Y2Rlfn7/m9vb5ufo+3t7SEhIKyxthcXFouQlNnZ2YqOk/v9/9jY2aysrOTk5J6hp52hp/Ly8vj6/qqqqvn6/oiMkCcoKlNTUzU1NXJycvf5/fn8/nR0dLu7u6+wsvDw8IWJjoaJjmxsbPT3+vT3++Hh4VBQUOzs7KywtrKysh0dHt/f3xMUFRMTE/b3+iMkJrGxsSwtLjg4N0RFSLC0uNHR0fX19ZecoZiboRAQEH19fcvQ1p2ip9TW2YaGho2Rlo6Rlvj7/+7w9Pn7/4mMkLW2uPT2+Y+Tlw8PDyEiJJ+fn/r+/xUXGBUVFZaWligpK/r6+vr8/vr8/6qsrZmeo/b4/KWlpfz+/9XV1fX3++bm5vX3+j0+QAwLC62wtmBgYJycnBkZGQAAAP///////xr+LuQAAAEAdFJOU////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////wBT9wclAAAGvUlEQVR42mL4TwzYuuqc6FuiVP4HCCAGoszrtP3rxJBJlIEAAUSMgZPUmfNuKrguJMpEgAAiwsDG68xRYf9aXrz4OYcIAwECiKCB/750Mit6//v3L9jiitDafwQNBAggQgb+a7zErOj3DwROW9gK1RM0ESCACBjoc3I181mIef/+8S6yPVxMyESAAMJvoE/jambF2H8wcHrRicPrCZgIEEB4DfT5sJpZEmEe0MQa14UEfA0QQPgM/DfpMnOU9z9kcMSCUW4bXhMBAgifgcWX0M37B0w9jAz9+AwECCA8Bt5TxzTv379cEVepEjwGAgQQbgMzr+tiMe/fv+4T1lLNuA0ECCCcBi57z8xW9Q8b2MFoIToFp4EAAYTLwE/zmNli/2EHwVesZXxwGQgQQDgMnGLGvNnvHy4QLO8mgyuqAQIIu4E+XLqbw/7hBu3ybkw4TAQIIKwG+szTDYz1wmOgV/5jR33sJgIEEANW836Y+yGZdyh3RVpa2jrfCoRQQv4PG+wmAgQQFgMLWHU3X0B2X5keo5qa2g/tqUhiCcE/bLCGI0AAYRpYsJiZLRHFv/GMf0EgDyUVxQBNPI/FRIAAwjCwZCnQPKgreH6DaV8RsIH+AWDe1R1eMDcexWIiQAChG9i/+28ULL3M1LVJxjCQh1GXHSqf/+OPAUZ6BAggNAMb9vyNyoAoD5dl/vv3TAqagb5H//7VnQlPj5gmAgQQqoEdOnDziriZQaZooRr4lRvEYs6bBTPRGj0XAgQQioHSs/9GQdOz3wuwIX+ZhZENDNWCiP61jIGZ+BytpAAIIGQD9yf91YKXL6nMEK26mv+SoQYe+7cJah4zJ0xZfo0IwzJkAwECCMnA6qS/gUjllQBUs8jHRIhrjVN6daHmpSKU5Z84jtKmAAgghIGFIX8DkfNvOQvURD0NUzC9absaVEQApcStcZVDMhEggOAGRk/+G4daHiw5AdV/xQFMnVkE5bPMQK8VhBBtCoAAghr4jyP9rwB6+ZwNDUY0cCILvTT7I49oUwAEEMRAH5B5y9HLlAR2bObptmOUQ91/bIVgLQCAAAIbGMmR8zcCS/3hMQ2LgX2zsNQzi+AmAgQQyMDIiUDzWrGVe7tkMczbUoFN4WmgiZA2BUAAAQ2MjH70NwKp/vAK94CB8IAzaObJzkeSRTUR0qYACCCG/z7A9DIdOZzXcIp5QsH2XmFU85j31sLkPOf2ocYMpE0BEEAM/6pD/sahxFuR5V/iQCBqPQNsU7z59x8ggBg4kv7WocZHihJZBv7LPWHB0PEfIIAY5P5yr/xHFQP/xT++cu0/QAAxcFHPwIwrdjf+AwQQw90Qank5aIHJaa//AAFEvUgRXGDZFPP/P0AA4U82nr2/iE02giwbS0OB6RAggAglbG4iE3a8VZsEyLz/AAFEpawXb2XSBTbvP0AAkV44iL/GVBhkZaICMe8/QACRUXxlYxRfQSxtKjHQ8hAggMgoYEWK0NQls7B0ecFKbIAAIqYKeKwAqVssYHUoahVQaWSpgqhTAAKIiErKUnMBmNaexoitkopfwPIAqdYDCCDC1SjjU3g1KsaMWY0KLjhVilwvAwQQ4Ype7F8QtKKf+u8gRkVfZvREFaXlABBABJsiB5GbIhcl0ZoiQQruTahtG4AAwt1YsgQ7MS4UpbF0ABywzALlMPOOGqK1vgACCHdzrjwPaKLRAbTmXLYTMBRgQRik4PQOvX0IEED4GpypzGrZGA1O8Su6GrDwU3Dkx2jBAgQQviZxDOdLLE3i2iMJMPfZ82G2sQECiOxGuxfQPEMsvQCAAMLWrdBBMxFrtyL5qDM/tn4KQABh6/g0b2BmI9Dx8ZqgoIDVvP8AAYS1a1aQxEygazbBxf0d9r4eQABh7zyWsDKzfcZjYI+C3U4cvVGAAMLRvX02mTnKD595fLj6ywABhKsD3nAfGI64zHNwwWnef4AAwjlEcCfpryL2IYIJpjj9CwQAAYR7EKPjFnoLAFafo5R/6AAggPAMs0y6jc1EoHnt+MZtAAII30DQN2CbAt3EZGD7wAufgQABhHeoqhDDxOQFkPYBbgAQQHgH00BtiijkwZZ4hTMS+M37DxBA+If7Iic+Qm5TxFudUSZg3n+AACIwIAlu98DSY6WViXIMoRFOgAAiNGTqE50OM7HSCNE+wA0AAojwoC6wTQFu9yQbveryIjxKDBBARAw7A30NbFNUGpkoEzOODRBAxAyMP2T9ayyux7KDqJF2gAAiauh+XxLz3zOlxM0FAAQQUQb+35fzXZU48/4DBBDDfyoDgAADAEid0CPyPiP4AAAAAElFTkSuQmCC';
 case 'condition':
 return 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAFAAAABPCAMAAABLT7LRAAAABGdBTUEAAK/INwWK6QAAABl0RVh0U29mdHdhcmUAQWRvYmUgSW1hZ2VSZWFkeXHJZTwAAAMAUExURQ0NDQMDA62vsRQUFE5OTt/f3/j7//b6/e7w8xISEu3v8klJSXt+gkVFRaqqqoqOky0tLqGhoVVUVLe3uSYmJikpKaytr6mqrIeLjxgYGHd3d4qNkjg4OHFxcQgJCXp8gSgpKjw8PEBAQCoqKiAgIBwcHCQkJB8eHigqKycoKSIiJCYnKBQUEyEhIhMTE/b29sTExHNzc6Ojo93d3UNDQ/7+/tjY2P7//7a2ttbW1jo6OsbGxvP1+Dw8O/7+/zs7OzY2NoiMkYmMkZicofv7++fn5/z8/Pv//4mNkjg3Nzc3N9zc3NfX1/b5/PL094+Tl/b4+7y8vPv+/8PDw56hp7GxscXFxdTU1PX3+tLS0rS0tIiMkPP2+bS1to+Sl7i4uv39/cLCwsvLy56ip7K0tevr67W1tfr9/yMkJdbW1/b4/ImNkezu8cfHx9XV1fP2+uHh4Zicouzs7Jmcofz//66urlBQUNPT0/f5/by9vnh4eDo6OfL1+PT3+igoJ+Dg4JmdorOzs/j8/7KyspufpCkqK8rKyru8vR8gIY6Rlu7u7omMkIuQlIaJjtvb25ufoyUmJwwMDBUVFPT2+Y2RlhgYF+7v8iQkI/r+/8LCw/f6/vX3+4+TmHl8gE9PT4WJjvn8/7i5upmcou3t7f3///j4+EJCQvH096+wspCTmOPj48jIyCEiIxYWFqqrraGmqY2NjR4eHUlLTpqdou7x9Lu7u35+fiosLisrK5ubm5mdoxAQD29vb7u7vJyfpevt8FlZWY2RlRgXF/P1+R4fIPb5/ff5/KSoroGEiPr8/3p9gbi4uA8PD1NTU76+vggICNjY2WNjY+jo6Pf397CxsqysrKqsrScnJ/T3+3h7gLW2t56iqMbL0sTExQoLC8XGxp2hpvT09Le3t5OTk/j6/bKztdTW2fLy8vz+/yQlJiUlJSIiIZiYmHR0dE5NTU5OTZmdoYqKigUFBvv9/6enp4ODg4aGhvb3+hwdH7W2uJGVmrCwseLi4gAAAP///////yogxfoAAAEAdFJOU////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////wBT9wclAAAGHElEQVR42mL4Twyo7dEMqSBK5X+AAGIgyjxHxr9niokzESCAiDFwctxf3jcCn5J9iTEQIICIMNDG/699+L9EkU/JAUQYCBBABA385+fxV9Hs379/YdcZG2b/I2ggQAARMtDcxuOvreU/EEi8zlgYQNBEgAAiYKCdzRyYeUAT6xhDvpsTMBAggPAbaFez8q9t5j8YmAk0MYCAiQABxIDfff5/PX3+IUDw9E8hWfhNBAggfAb+i3H6a2/2Dxncvc7o7Is3HAECCJ+BERjmAeNahNH5CT4DAQIIj4G+nX+r0c3798/61adkNjwGAgQQbgNnTcFmHtBENbnkBbgNBAggnAayOf6V8f6HDSS9Pr/rM04DAQIIl4FN3Yyelv+wg1xduf4XuAwECCAcBs7IZvTCZR7QRBW5fUtwGAgQQNgNDIr75JD3DzfIVb+3HkdyBAggrAa6R3+y8jHFY6Bpku7EfuwmAgQQNgNLoj85WCKZV//1bVVVlezOxcgmqk5swZrAAQIIi4HuEp8cIpHdZ63KeP78+b/crEhiCdaqEzdjcyNAAGEa6N7DyG2J4l8Dvb8gsAwlFZlaqzIkYzERIIAwDAzi/1uGlv4MmMEGmhijpXDVMw8w4xoggNANTJ//t+zPP6IM/Je0cWJxCbqBAAGEZuCC3r/z6v8RaeA/6433nIPQDAQIIFQDj0r8nVfwj2gD/1mr3/uQjmogQAChGLhc+i/Lqn8kGPjPWkXu9kkUAwECCNnALTf/Crn+I8nAf20Tzi+ahWwgQAAhGcgx9++8S/9INPDf6umfnJHbFAABhDCQa+7fMtd/JBv4L/jVJ+cshIEAAQQ3kEP+7zzs5hEwENimYCxGtCkAAghq4L+cU3+FUv+RZeC/sB1/EW0KgACCGGiaw/NXqOgfmQb+u7+DsTACaiJAADHAzJu3+B/ZBv4Lq2NcFAHJ2AABBDLQdBrQvFTcpV/YJLCBvLgNBLUpCiFtCoAAYgCZd+IvC5J/TZfqo4B/5yCljVKeG6rEUuS4BrZSwG0KgABi+G/KxfP3bBeS3POPsobIQOqAHNjAjRelUMT3PEZJjzsgbQqAAGL4x3Xq73tk8/6tqvtLHLBCzTPANsWxf/8BAohh/6m/71DDz1WULAOBbYp7DYf/AwQQQ8tfq8v/qGLgv3Idxjv/AQKI4fhfrwIqGRh8Zs2z/wABxLDcEb0JQ66BUZytV0z/AwQQw798YKOtD1mi4DpZBj5i1kxx+/8fIIAY/psDm5VKyCbqNwsaIQO+bQxg/Ts2fEERFzyNbJ7FVIVYF2A6BAggBkjD19YHTzvBei3YQJR6GR2ETmptB5n3HyCAGMBNc2nkpjkZeRlkXjzYvP8AAQQuHOz8gCaaUWBgGty8/wABBCm+zIHdJaULZBuYNlUh3g1aHgIEEAOiA6ZkRqaBFocmLDSFldgAAQSvAmyyQV1Ecgxs1NEURtQpAAGEqKSAqccqnAwDo/S0fyLVegABhFSN5jv+tTIj2cBAzgmxyPUyQAAhV/Q3HCEdWVIMDNR7+RSl5QAQQChNEfaDfz3DSTLQ4syaFNS2DUAAoTaWZkn89SonwUALhg4xtNYXQAChNeeaev96lhNtoAXDkWvo7UOAAMLS4MTooOAyMJDhhzhGCxYggLA0iYFNdmIMTLCYqCuG2cYGCCBsjXa0TsC/YE4sjXY3i4kdWMz7DxBAxHQr2pQ5JSUlJwrVo7lPHFs/BSCAiOn4FEVdLS0t5TN4iGRe4MQ1WM37DxBAZHbNAndrrcPe1wMIIPI6jxYMGStw9EYBAois7q3FmQwxXP1lgAAipwNusTuDCWePHiCAyBgi2LpbawXuMQeAACJ9ECONU/MWnlERgAAieZgFWJ4K4xu3AQggUgeCGoHtA1N8BgIEEIlDVc2TIO0D3AAggEgbTAvlFGjHb95/gAAiabgvdJJAJQHz/gMEECkDkr8ntVa6ERrhBAggEoZMQych2ge4AUAAET+oazFJY6Ep4VFigAAieti5UWd7JTHj2AABROzA+LeN2nuJGmkHCCCih+4FYombCwAIIKIM/J//a1MKceb9Bwgghv9UBgABBgDE9AegTtRWBQAAAABJRU5ErkJggg==';
 case 'block':
 return 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAAXNSR0IArs4c6QAAAOJJREFUOBGtlAEOwiAMRcFo4jyaiVfwcF7ByPWwX9JWfjBlkSYbf2N9/QWyXGt9pZSucq2IkgVYV5CUcVRxf6r6PaJ0zimpBdUYH7eWZ8ANSiYS/GJEqN7RQw9sGL9HBXheMg14OcWGOH/UgAFHLfOaufWmuADeGvAs6nuRMclAnsc3HAZEy3sjbjkiUo/0+Mk2h9uMQ7Y0IDoQayg1NAflVGOcjQ4YJfGm8KYh34AzmzICsAkD4hxGLfKSMKxzOHOwueWRA3eIXWaLM5bIpgNN+ReRI66PzOU/2INAi3v6W5U39EBaJ8n82IkAAAAASUVORK5CYII=';
 */