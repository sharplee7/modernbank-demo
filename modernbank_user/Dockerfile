# Build stage
FROM golang:1.23-alpine AS builder
WORKDIR /app

# Copy go mod and sum files
COPY go.mod go.sum ./
# Download dependencies
RUN go mod download
# Copy source code
COPY . .
# Build the application
RUN CGO_ENABLED=0 GOOS=linux go build -o bank-user ./cmd/main.go

# Final stage
FROM alpine:latest
WORKDIR /app
# Copy binary from builder
COPY --from=builder /app/bank-user .
# Expose port
EXPOSE 8091
# Run the binary
CMD ["./bank-user"]