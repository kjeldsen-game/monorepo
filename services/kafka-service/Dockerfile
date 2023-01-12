FROM rust:1.65.0
COPY . .
RUN cargo build --release
CMD ["./target/release/kafka-service"]
