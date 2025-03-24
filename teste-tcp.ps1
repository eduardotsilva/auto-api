# Script para testar conexão TCP com o servidor de rastreamento

Write-Host "Conectando ao servidor TCP na porta 5001..."

try {
    $client = New-Object System.Net.Sockets.TcpClient
    $client.Connect("159.65.43.73", 5001)
    $stream = $client.GetStream()
    $writer = New-Object System.IO.StreamWriter($stream)
    $reader = New-Object System.IO.StreamReader($stream)

    Write-Host "Conectado com sucesso!"
    Write-Host "Enviando mensagem de teste..."

    # Mensagem de teste no formato GPRMC
    $mensagem = "`$GPRMC,123456.789,A,1234.5678,N,5678.1234,W,0.0,0.0,240324,0.0,E,A*00"
    $writer.WriteLine($mensagem)
    $writer.Flush()

    Write-Host "Mensagem enviada: $mensagem"
    Write-Host "Aguardando resposta..."

    # Aguarda um pouco para ver se há resposta
    Start-Sleep -Seconds 2

    # Fecha a conexão
    $client.Close()
    Write-Host "Conexão fechada."

} catch {
    Write-Host "Erro ao conectar: $_"
} 