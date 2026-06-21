#!/bin/bash
set -e

PROJECT_DIR="$(cd "$(dirname "$0")" && pwd)"

setup_java() {
  if [ -z "$JAVA_HOME" ]; then
    for jdk in /opt/jdk-17* /opt/jdk-21* /opt/zulu* /usr/lib/jvm/java-17* /usr/lib/jvm/java-21*; do
      if [ -d "$jdk" ]; then
        export JAVA_HOME="$jdk"
        break
      fi
    done
  fi
  export PATH="$JAVA_HOME/bin:$PATH"
  if [ -d "/opt/apache-maven-3."* ]; then
    export PATH="$(ls -d /opt/apache-maven-3.* | sort -V | tail -1)/bin:$PATH"
  fi
}

setup_node() {
  for node_dir in /opt/node-v20* /opt/node-v22*; do
    if [ -d "$node_dir" ]; then
      export PATH="$node_dir/bin:$PATH"
      break
    fi
  done
}

setup_java
setup_node

echo "=== 编译后端 ==="
cd "$PROJECT_DIR/backend"
mvn package -DskipTests -q

echo ""
echo "=== 启动后端 (端口 3001) ==="
java -jar target/admin-server-1.0.0.jar &
BACKEND_PID=$!
echo "后端 PID: $BACKEND_PID"

echo ""
echo "=== 启动前端 (端口 5173) ==="
cd "$PROJECT_DIR/frontend"

if [ ! -d "node_modules" ]; then
  echo "安装前端依赖..."
  npm install --silent
fi

npx vite --host 0.0.0.0 &
FRONTEND_PID=$!
echo "前端 PID: $FRONTEND_PID"

echo ""
echo "=== 服务已启动 ==="
echo "前端地址: http://localhost:5173"
echo "后端地址: http://localhost:3001"
echo "登录账号: admin / admin123"

trap "echo '正在关闭服务...'; kill $BACKEND_PID $FRONTEND_PID 2>/dev/null" EXIT

wait
