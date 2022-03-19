####　クラスター　トーケン一覧取得
    kubeadm token list

####　ハッシュコートを取得する
    openssl x509 -pubkey -in /etc/kubernetes/pki/ca.crt | openssl rsa -pubin -outform der 2>/dev/null | openssl dgst -sha256 -hex | sed 's/^.* //'

####　ノード加入　加入までは時間がかかる。
    kubeadm join k8s-master:6443 --token zyofun.dflmtv1ddjpzuw7f \
        --discovery-token-ca-cert-hash sha256:↑で取得したハッシュコード \
    　　--ignore-preflight-errors=Swap
    
    ※　ホスト：ポット　トーケン　ハッシュコード　この四つのパラメータを間違ってしないように入力してください。単純のコピーはダメです
    
#### もし、使用期間が切れた場合は、新たなトーケンを発行する
    kubeadm token create --print-join-command
    
#### podsステータス
    kubectl get pods --all-namespaces
    
#### flannel ネットワーク　プラグイン　インストール master nodeで実行
    vi /etc/hosts/
    ipaddress raw.githubusercontent.com を追加、　ipaddressをネットワークで検索して
    kubectl apply -f https://raw.githubusercontent.com/coreos/flannel/master/Documentation/kube-flannel.yml  
    ※時間がかかります  

