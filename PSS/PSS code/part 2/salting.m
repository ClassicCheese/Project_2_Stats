% ask for file names and parameters
fileNameIn = input('Enter input file name: ', 's');
fileNameOut = input('Enter output file name (leave blank to store in similar name as input): ', 's');
range = input('Enter range to salt: ');

if ~endsWith(fileNameIn, '.csv', 'IgnoreCase', true)
fileNameIn = strcat(fileNameIn, '.csv');
end

if strcmp(fileNameOut, '')
    fileNameOut = strrep(fileNameIn, '.csv', '_salted.csv');
else
    if ~endsWith(fileNameOut, '.csv', 'IgnoreCase', true)
    fileNameOut = strcat(fileNameOut, '.csv');
    end
end

data = csvread(fileNameIn);

%store data 
y_data = data(:, 2);
x_data = data(:, 1);
rng = randi([0, 1], size(y_data));
salt = (rng * 2) - 1; %this will be either 1 or -1
salt_amount = salt * range;
y_salted = y_data + salt_amount;

figure;
title('Salted graph');
xlabel('value of x');
ylabel('Seasoned cos(x)');
plot(x_data, y_salted);
grid on

xy_salted = [x(:), y_salted(:)];
csvwrite(fileNameOut, xy_salted);
disp(['Data sotred in: ', fileNameOut]);