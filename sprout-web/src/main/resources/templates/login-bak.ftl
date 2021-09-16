<!DOCTYPE html>
<html>
<head>
    <#assign ctx=ctx.contextPath/>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>Haze | 用户登陆</title>
    <!-- Tell the browser to be responsive to screen width -->
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
    <!-- Bootstrap 3.3.7 -->
    <link rel="stylesheet" href="${ctx}/resources/adminLTE/bower_components/bootstrap/dist/css/bootstrap.min.css">
    <!-- Font Awesome -->
    <link rel="stylesheet" href="${ctx}/resources/adminLTE/bower_components/font-awesome/css/font-awesome.min.css">
    <!-- Ionicons -->
    <link rel="stylesheet" href="${ctx}/resources/adminLTE/bower_components/Ionicons/css/ionicons.min.css">
    <!-- Theme style -->
    <link rel="stylesheet" href="${ctx}/resources/adminLTE/dist/css/AdminLTE.min.css">
    <!-- iCheck -->
    <link rel="stylesheet" href="${ctx}/resources/adminLTE/plugins/iCheck/square/blue.css">
    <link type="text/css" rel="stylesheet" href="${ctx}/resources/threejs/main.css">
    <script type="text/javascript">
        <!-- 解决登录页面嵌套问题 -->
        if (window !== top){
            top.location.href = location.href;
        }
    </script>
</head>
<body class="hold-transition login-page">
<div id="threejs"></div>
<div class="login-box" style="z-index: 9999; position: absolute; top: 15%; left:40%;">
    <div class="login-logo">
        <a href="#"><b>Haze综合管理平台</b></a>
    </div>
    <!-- /.login-logo -->
    <div class="login-box-body" >
        <p class="login-box-msg">请输入账号信息</p>

        <form action="${ctx}/login" method="post" onsubmit="return validator()">
            <div class="form-group has-feedback">
                <input type="text" class="form-control" placeholder="请输入用户名" id="username" name="username">
                <span class="glyphicon glyphicon-user form-control-feedback"></span>
            </div>
            <div class="form-group has-feedback">
                <input type="password" class="form-control" placeholder="清输入密码" id="password" name="password">
                <span class="glyphicon glyphicon-lock form-control-feedback"></span>
            </div>
            <div class="row">
                <div class="col-xs-8">
                    <div class="checkbox icheck">
                        <label>
                            <input type="checkbox"> 记住我
                        </label>
                    </div>
                </div>
                <!-- /.col -->
                <div class="col-xs-4">
                    <button type="submit" class="btn btn-primary btn-block btn-flat">登陆</button>
                </div>
                <!-- /.col -->
            </div>
        </form>
    </div>
    <!-- /.login-box-body -->
</div>
<!-- /.login-box -->

<!-- jQuery 3 -->
<script src="${ctx}/resources/adminLTE/bower_components/jquery/dist/jquery.min.js"></script>
<!-- Bootstrap 3.3.7 -->
<script src="${ctx}/resources/adminLTE/bower_components/bootstrap/dist/js/bootstrap.min.js"></script>
<!-- iCheck -->
<script src="${ctx}/resources/adminLTE/plugins/iCheck/icheck.min.js"></script>
<script>

    $(function () {
        $('input').iCheck({
            checkboxClass: 'icheckbox_square-blue',
            radioClass: 'iradio_square-blue',
            increaseArea: '20%' /* optional */
        });
    });
    function validator() {
        var username = $("#username").val();
        var password = $("#password").val();
        if (username === '' || password === '') {
            alert("用户名或密码不能为空");
            return false;
        }
        return true;
    }
    function reloadValidateCode() {
        $("#validateCodeImg").attr( "src", "validateCode?date = " + new Date()+ Math.floor(Math.random() * 24));
    }
    function refreshCode() {
        $("#validatecode").attr('src',"${ctx}/validateCode?t="+Math.random());
    }
</script>
<script type="x-shader/x-vertex" id="vertexshader">
			attribute float scale;
			void main() {
				vec4 mvPosition = modelViewMatrix * vec4( position, 1.0 );
				gl_PointSize = scale * ( 300.0 / - mvPosition.z );
				gl_Position = projectionMatrix * mvPosition;
			}
</script>
<script type="x-shader/x-fragment" id="fragmentshader">
			uniform vec3 color;
			void main() {
				if ( length( gl_PointCoord - vec2( 0.5, 0.5 ) ) > 0.475 ) discard;
				gl_FragColor = vec4( color, 1.0 );
			}
</script>
<script src="${ctx}/resources/threejs/three.js"></script>
<script>
    var SEPARATION = 100, AMOUNTX = 50, AMOUNTY = 50;
    var container;
    var camera, scene, renderer;
    var particles, count = 0;
    var mouseX = 0, mouseY = 0;
    var windowHalfX = window.innerWidth / 2;
    var windowHalfY = window.innerHeight / 2;
    init();
    animate();

    function init() {
        //container = document.createElement('div');
        //document.body.appendChild(container);
        container = document.getElementById('threejs');
        camera = new THREE.PerspectiveCamera(75, window.innerWidth / window.innerHeight, 1, 10000);
        camera.position.z = 1000;

        scene = new THREE.Scene();
        //scene.background = new THREE.Color(0x424242);
        //

        var numParticles = AMOUNTX * AMOUNTY;

        var positions = new Float32Array(numParticles * 3);
        var scales = new Float32Array(numParticles);

        var i = 0, j = 0;

        for (var ix = 0; ix < AMOUNTX; ix++) {

            for (var iy = 0; iy < AMOUNTY; iy++) {

                positions[i] = ix * SEPARATION - ((AMOUNTX * SEPARATION) / 2); // x
                positions[i + 1] = 0; // y
                positions[i + 2] = iy * SEPARATION - ((AMOUNTY * SEPARATION) / 2); // z

                scales[j] = 1;

                i += 3;
                j++;

            }

        }

        var geometry = new THREE.BufferGeometry();
        geometry.addAttribute('position', new THREE.BufferAttribute(positions, 3));
        geometry.addAttribute('scale', new THREE.BufferAttribute(scales, 1));

        var material = new THREE.ShaderMaterial({

            uniforms: {
                color: {value: new THREE.Color(0x3C8DBC)},
            },
            vertexShader: document.getElementById('vertexshader').textContent,
            fragmentShader: document.getElementById('fragmentshader').textContent

        });

        //

        particles = new THREE.Points(geometry, material);
        scene.add(particles);

        //

        renderer = new THREE.WebGLRenderer({antialias: true,alpha:true});
        renderer.setClearColor(0x000000,0);
        renderer.setPixelRatio(window.devicePixelRatio);
        renderer.setSize(window.innerWidth, window.innerHeight);
        container.appendChild(renderer.domElement);

        //stats = new Stats();
        //container.appendChild(stats.dom);

        document.addEventListener('mousemove', onDocumentMouseMove, false);
        document.addEventListener('touchstart', onDocumentTouchStart, false);
        document.addEventListener('touchmove', onDocumentTouchMove, false);

        //

        window.addEventListener('resize', onWindowResize, false);

    }

    function onWindowResize() {

        windowHalfX = window.innerWidth / 2;
        windowHalfY = window.innerHeight / 2;

        camera.aspect = window.innerWidth / window.innerHeight;
        camera.updateProjectionMatrix();

        renderer.setSize(window.innerWidth, window.innerHeight);

    }

    //

    function onDocumentMouseMove(event) {

        mouseX = event.clientX - windowHalfX;
        mouseY = event.clientY - windowHalfY;

    }

    function onDocumentTouchStart(event) {

        if (event.touches.length === 1) {

            event.preventDefault();

            mouseX = event.touches[0].pageX - windowHalfX;
            mouseY = event.touches[0].pageY - windowHalfY;

        }

    }

    function onDocumentTouchMove(event) {

        if (event.touches.length === 1) {

            event.preventDefault();

            mouseX = event.touches[0].pageX - windowHalfX;
            mouseY = event.touches[0].pageY - windowHalfY;

        }

    }

    //

    function animate() {

        requestAnimationFrame(animate);

        render();
        //stats.update();

    }

    function render() {

        camera.position.x += (mouseX - camera.position.x) * .05;
        camera.position.y += (-mouseY - camera.position.y) * .05;
        camera.lookAt(scene.position);

        var positions = particles.geometry.attributes.position.array;
        var scales = particles.geometry.attributes.scale.array;

        var i = 0, j = 0;

        for (var ix = 0; ix < AMOUNTX; ix++) {

            for (var iy = 0; iy < AMOUNTY; iy++) {

                positions[i + 1] = (Math.sin((ix + count) * 0.3) * 50) +
                    (Math.sin((iy + count) * 0.5) * 50);

                scales[j] = (Math.sin((ix + count) * 0.3) + 1) * 8 +
                    (Math.sin((iy + count) * 0.5) + 1) * 8;

                i += 3;
                j++;

            }

        }

        particles.geometry.attributes.position.needsUpdate = true;
        particles.geometry.attributes.scale.needsUpdate = true;

        renderer.render(scene, camera);

        count += 0.1;

    }

</script>
</body>
</html>
