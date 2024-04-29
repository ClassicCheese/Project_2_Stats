% ask user for parameters
%Calculating cos(x)
low = input('Enter the lower range of x :');
upp = input('Enter the upper range of x :');
incr = input('Enter the increment of x :');
filename = input('Enter file name to store data: ', 's');
if ~endsWith(filename, '.csv', 'IgnoreCase', true)
filename = strcat(filename, '.csv');
end

% now plot the the data
x = (low:incr:upp);
y = cos(x);
xy_data = [x, y];
xy_col = [x(:), y(:)];

figure;
plot(x, y);
title('cos(x) graph');
xlabel('value of x');
ylabel('cos(x)');
grid on;

% Store data in csv file
csvwrite(filename, xy_col);
disp(['Data stored in ', filename]);
