fn main() {
    dotenv::dotenv().ok(); // Load environment variables
    crons_service::run();
}
