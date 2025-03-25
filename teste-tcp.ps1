Write-Host "Conectando ao servidor TCP..."

try {
    $client = New-Object System.Net.Sockets.TcpClient
    $client.Connect("centerbeam.proxy.rlwy.net", 38254)
    $stream = $client.GetStream()
    $writer = New-Object System.IO.StreamWriter($stream)
    $reader = New-Object System.IO.StreamReader($stream)

    Write-Host "Conectado com sucesso!"
    Write-Host "Enviando mensagem de teste..."

    # Exemplo de mensagem (pode ajustar)
    $mensagem = "`$GPRMC,123456.789,A,1234.5678,N,5678.1234,W,0.0,0.0,240324,0.0,E,A*00"
    $writer.WriteLine($mensagem)
    $writer.Flush()

    Write-Host "Mensagem enviada: $mensagem"
    Write-Host "Aguardando resposta..."

    Start-Sleep -Seconds 2

    # Opcional: ler resposta se seu servidor responde
    if ($stream.DataAvailable) {
        $resposta = $reader.ReadLine()
        Write-Host "Resposta recebida: $resposta"
    } else {
        Write-Host "Nenhuma resposta recebida."
    }

    $client.Close()
    Write-Host "Conexão encerrada."

} catch {
    Write-Host "Erro ao conectar: $_"
}
