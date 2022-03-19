### インストールコマンド

yum install -y wget
yum install net-tools　※　netstat コマンド利用

ip addr

##### host name
    hostnamectl set-hostname k8s-master  

##### クラスター　ホスト設定
vi /etc/hosts

    アドレス　ホスト　別称
    192.168.112.130 k8s-master master
    192.168.112.131 k8s-node1 node1
    192.168.112.132 k8s-node2 node2
    
    ping master テスト
    
##### ファイアウォールの停止
    systemctl disable firewalld

##### selinux　の停止
    sed i 's/enforcing/disabled' /etc/selinux/config

##### 永久关闭swap分区
    sed -ri 's/.*swap.*/#&/' /etc/fstab

##### 将桥接的ipv4流量传递到iptables链
    cat > /etc/sysctl.d/k8s.conf << EOF
    net.bridge.bridge-nf-call-ip6tables = 1
    net.bridge.bridge-nf-call-iptables = 1
    EOF
    
    然后sysctl --system 使其生效

##### 时间同步操作
    yum install ntpdate -y
    
##### 安装docker
    
    wget https://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo -O /etc/yum.repos.d/docker-ce.repo
    yum -y install docker-ce-18.06.1.ce-3.el7
    systemctl enable docker && systemctl start docker
    docker --version
    
##### 添加阿里云yum软件源
    cat > /etc/docker/daemon.json << EOF
    {
    "registry-mirrors": ["https://b9pmyelo.mirror.aliyuncs.com"]
    }
    EOF
    
    systemctl restart docker
    
    cat > /etc/yum.repos.d/kubernetes.repo << EOF
    [kubernetes]
    name=Kubernetes
    baseurl=https://mirrors.aliyun.com/kubernetes/yum/repos/kubernetes-el7-x86_64
    enabled=1
    gpgcheck=0
    repo_gpgcheck=0
    gpgkey=https://mirrors.aliyun.com/kubernetes/yum/doc/yum-key.gpg
    https://mirrors.aliyun.com/kubernetes/yum/doc/rpm-package-key.gpg
    EOF

##### 指定版本安装k8s
    yum install -y kubelet-1.18.0 kubeadm-1.18.0 kubectl-1.18.0
    systemctl enable kubelet
    
##### k8s init 
    kubeadm init \
    --apiserver-advertise-address=192.168.112.130 \
    --image-repository registry.aliyuncs.com/google_containers \
    --kubernetes-version v1.18.0 \
    --service-cidr=10.96.0.0/12 \
    --pod-network-cidr=10.244.0.0/16
    
#####
    mkdir -p $HOME/.kube
    sudo cp -i /etc/kubernates/admin.conf $HOME/.kube/config
    sudo chown $(id -u):$(id -g) $HOME/.kube/config 
