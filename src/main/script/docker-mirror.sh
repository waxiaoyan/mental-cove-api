#!/bin/bash

# 定义新的 registry mirrors 配置
NEW_MIRRORS=(
  "https://proxy.1panel.live"
  "https://docker.1panel.top"
  "https://docker.m.daocloud.io"
  "https://docker.1ms.run"
  "https://docker.ketches.cn"
)

# 定义 daemon.json 文件路径
DAEMON_JSON="/etc/docker/daemon.json"

# 检查文件是否存在，不存在则创建
if [ ! -f "$DAEMON_JSON" ]; then
  echo "{}" > "$DAEMON_JSON"
fi

# 使用 jq 工具修改配置文件
if command -v jq &> /dev/null; then
  # 读取现有配置
  CURRENT_CONFIG=$(cat "$DAEMON_JSON")

  # 更新 registry-mirrors
  UPDATED_CONFIG=$(echo "$CURRENT_CONFIG" | jq --argjson mirrors "$(printf '%s\n' "${NEW_MIRRORS[@]}" | jq -R . | jq -s .)" '."registry-mirrors" = $mirrors')

  # 写入新配置
  echo "$UPDATED_CONFIG" > "$DAEMON_JSON"

  echo "daemon.json 已成功更新"

  # 重启 Docker 服务使更改生效
  if systemctl is-active --quiet docker; then
    echo "正在重启 Docker 服务..."
    systemctl restart docker
    echo "Docker 服务已重启"
  else
    echo "Docker 服务未运行，无需重启"
  fi
else
  echo "错误: jq 工具未安装，请先安装 jq"
  echo "在基于 Debian 的系统上可以使用: sudo apt-get install jq"
  echo "在基于 RHEL 的系统上可以使用: sudo yum install jq"
  exit 1
fi